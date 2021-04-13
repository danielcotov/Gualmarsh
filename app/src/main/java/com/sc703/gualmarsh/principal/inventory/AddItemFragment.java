package com.sc703.gualmarsh.principal.inventory;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.database.models.product.Product;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class AddItemFragment extends Fragment {
    private final FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference bdRef = fDatabase.getReference();
    private ItemViewModel viewModel;
    private EditText edtName, edtCode, edtQuantity, edtDescription, edtPrice;
    private ImageView imvAddPhoto, imvClose;
    private ProgressBar progressBar;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TextView tvDate;
    private Button btnCancel, btnDiscard;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private Uri imagePath;
    private StorageReference storage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_item, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        imvAddPhoto = root.findViewById(R.id.addItem_addPhoto);
        progressBar = root.findViewById(R.id.addItem_Progressbar);
        imvClose = root.findViewById(R.id.addItem_Close);
        edtName = root.findViewById(R.id.edt_addItem_productName);
        edtCode = root.findViewById(R.id.edt_addItem_barcode);
        edtQuantity = root.findViewById(R.id.edt_addItem_quantity);
        edtDescription = root.findViewById(R.id.edt_addItem_description);
        edtPrice = root.findViewById(R.id.edt_addItem_price);
        tvDate = root.findViewById(R.id.tv_datePicker);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Material_Dialog_MinWidth, dateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                tvDate.setText(date);
            }
        };
        imvAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(v);
            }
        });
        imvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edtCode.getText().toString().isEmpty() | !edtName.getText().toString().isEmpty() | !edtDescription.getText().toString().isEmpty()
                        | !edtPrice.getText().toString().isEmpty() | !edtQuantity.getText().toString().isEmpty()) {
                    builder = new AlertDialog.Builder(v.getContext());
                    View view = getLayoutInflater().inflate(R.layout.close_popup, null, false);
                    btnCancel = view.findViewById(R.id.btn_discardPopup_Cancel);
                    btnDiscard = view.findViewById(R.id.btn_discardPopup_Discard);

                    builder.setView(view);
                    dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.DIM_AMOUNT_CHANGED);
                    dialog.show();

                    btnDiscard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_principal_fragment);
                            navController.navigate(R.id.action_Add_to_Products);
                            dialog.dismiss();
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                } else {
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_principal_fragment);
                    navController.navigate(R.id.action_Add_to_Products);
                }
            }
        });


        Button btnSave = root.findViewById(R.id.btn_addItem_Save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference product = bdRef.child("productCategories/" + viewModel.getCategoryCode().getValue());
                Map<String, Object> productAdd = new HashMap<>();
                if (addItem(v)) {
                    try{
                        int productKey = Integer.parseInt(viewModel.getProductCount().getValue()) + 1;
                        productAdd.put(Integer.toString(productKey), new Product(edtCode.getText().toString(), edtName.getText().toString(), edtDescription.getText().toString(),
                                Long.parseLong(edtPrice.getText().toString()), Long.parseLong(edtQuantity.getText().toString())));
                        product.updateChildren(productAdd);
                        uploadImage(v);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

        });

        return root;
    }
    private boolean validateCode(String code) {
        if (code.isEmpty()) {
            edtCode.setError(getText(R.string.emptyField));
            return false;
        } else {
            edtCode.setError(null);
            return true;
        }
    }
    private boolean validateName(String name) {
        if (name.isEmpty()) {
            edtName.setError(getText(R.string.emptyField));
            return false;
        } else {
            edtName.setError(null);
            return true;
        }
    }

    private boolean validateDescription(String description) {
        if (description.isEmpty()) {
            edtDescription.setError(getText(R.string.emptyField));
            return false;
        } else {
            edtDescription.setError(null);
            return true;
        }
    }
    private boolean validatePrice(String price) {
        if (price.isEmpty()) {
            edtPrice.setError(getText(R.string.emptyField));
            return false;
        } else {
            edtPrice.setError(null);
            return true;
        }
    }
    private boolean validateQuantity(String quantity) {
        if (quantity.isEmpty()) {
            edtQuantity.setError(getText(R.string.emptyField));
            return false;
        } else {
            edtQuantity.setError(null);
            return true;
        }
    }
    public boolean addItem(View view) {
        String code = edtCode.getText().toString();
        String name = edtName.getText().toString();
        String description = edtDescription.getText().toString();
        String price = edtPrice.getText().toString();
        String quantity = edtQuantity.getText().toString();

        if (validateCode(code) | validateName(name) | validateDescription(description) | validatePrice(price) | validateQuantity(quantity)) {
            return true;
        } else {
            return false;
        }
    }
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                imagePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imagePath);
                    imvAddPhoto.setBackgroundColor(0x292929);
                    imvAddPhoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "An issue occurred while loading the image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    });
    private void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        resultLauncher.launch(Intent.createChooser(intent, "Select an image"));
    }
    private void uploadImage(View view){
        storage = FirebaseStorage.getInstance().getReference().child("Resources/Products");

        if (imagePath != null){
            StorageReference ref = storage.child(edtCode.getText().toString() + ".jpg");
            ref.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_principal_fragment);
                    navController.navigate(R.id.action_Add_to_Products);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = ((100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount());
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress((int)progress);
                }
            });
        }
    }
}