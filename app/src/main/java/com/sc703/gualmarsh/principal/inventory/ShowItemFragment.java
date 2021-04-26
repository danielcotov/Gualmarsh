package com.sc703.gualmarsh.principal.inventory;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.database.models.itemView.ItemViewModel;
import com.sc703.gualmarsh.database.models.product.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class ShowItemFragment extends Fragment {

    private final FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference bdRef = fDatabase.getReference();
    private ItemViewModel viewModel;
    private ImageView imvShowImage, imvClose, imvDelete;
    private EditText edtName, edtQuantity, edtCode, edtDescription, edtPrice, edtTotalPrice;
    private String bName, bQuantity, bCode, bDescription, bPrice;
    private Button btnSave, btnCancel, btnDelete, btnDiscard, btnDisCancel;
    private StorageReference storage;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TextView tvDate;
    private Uri imagePath;
    private LinearLayout btn_export;
    private ProgressBar progressBar;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_show_item, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        btn_export = root.findViewById(R.id.showItem_Export);
        imvShowImage = root.findViewById(R.id.showItem_Photo);
        edtName = root.findViewById(R.id.edt_showItem_productName);
        edtCode = root.findViewById(R.id.edt_showItem_barcode);
        edtQuantity = root.findViewById(R.id.edt_showItem_quantity);
        edtDescription = root.findViewById(R.id.edt_showItem_description);
        edtPrice = root.findViewById(R.id.edt_showItem_price);
        edtTotalPrice = root.findViewById(R.id.edt_showItem_totalPrice);
        imvClose = root.findViewById(R.id.showItem_Close);
        btnSave = root.findViewById(R.id.btn_showItem_Save);
        progressBar = root.findViewById(R.id.showItem_Progressbar);
        imvDelete = root.findViewById(R.id.imv_showItem_Delete);
        tvDate = root.findViewById(R.id.tv_showItem_datePicker);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        edtName.setText(viewModel.getProductName().getValue());
        edtCode.setText(viewModel.getProductCode().getValue());
        edtQuantity.setText(viewModel.getProductQuantity().getValue());
        edtDescription.setText(viewModel.getProductDescription().getValue());
        edtPrice.setText(viewModel.getProductPrice().getValue());
        int totalPrice = Integer.parseInt(viewModel.getProductPrice().getValue().substring(1)) * Integer.parseInt(viewModel.getProductQuantity().getValue());
        edtTotalPrice.setText("¢" + Integer.toString(totalPrice));
        tvDate.setText(viewModel.getProductExpiration().getValue());
        loadImage(getContext(), imvShowImage, viewModel.getProductCode().getValue());
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_principal_fragment);


        bName = edtName.getText().toString();
        bCode = edtCode.getText().toString();
        bQuantity = edtQuantity.getText().toString();
        bDescription = edtDescription.getText().toString();
        bPrice = edtPrice.getText().toString();
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((!s.toString().equals(bName)) && (!s.toString().equals(bCode)) && (!s.toString().equals(bQuantity)) &&
                        (!s.toString().equals(bDescription)) && (!s.toString().equals(bPrice))){
                    btnSave.setVisibility(View.VISIBLE);
                    try{
                        int totalPrice = Integer.parseInt(edtPrice.getText().toString().substring(1)) * Integer.parseInt(edtQuantity.getText().toString());
                        edtTotalPrice.setText(Integer.toString(totalPrice));
                    } catch (Exception e){
                        e.printStackTrace();
                    }


                }else{
                    btnSave.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        edtName.addTextChangedListener(watcher);
        edtCode.addTextChangedListener(watcher);
        edtDescription.addTextChangedListener(watcher);
        edtPrice.addTextChangedListener(watcher);
        edtQuantity.addTextChangedListener(watcher);
        tvDate.addTextChangedListener(watcher);

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

        btn_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder data = new StringBuilder();
                data.append("Product Name,Quantity,Barcode,Price,Description");
                data.append("\n").append(String.valueOf(viewModel.getProductName().getValue())).append(",").append((viewModel.getProductQuantity()).getValue()).append(",").
                        append(String.valueOf(viewModel.getProductCode().getValue())).append(",").append(viewModel.getProductPrice().getValue()).append(",").
                        append(String.valueOf(viewModel.getProductDescription().getValue()));
                try{
                    FileOutputStream out = getContext().openFileOutput("data.csv", Context.MODE_PRIVATE);
                    out.write((data.toString()).getBytes());
                    out.close();

                    Context context = getContext();
                    File fileLocation = new File(getContext().getFilesDir(), "data.csv");
                    Uri path = FileProvider.getUriForFile(context, "com.sc703.gualmarsh.FileProvider", fileLocation);
                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                    fileIntent.setType("text/csv");
                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                    startActivity(Intent.createChooser(fileIntent, "Open with"));
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        imvShowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference productCategory;

                if (navController.getPreviousBackStackEntry().getDestination().toString().contains("productFragment")){
                    productCategory = bdRef.child("productCategories/" + viewModel.getCategoryCode().getValue());
                }else{
                    productCategory = bdRef.child("productCategories/" + viewModel.getProductCategory().getValue());
                    viewModel.setCategoryCode(viewModel.getProductCategory().getValue());
                }
                DatabaseReference product = bdRef.child("products/");
                Map<String, Object> productAdd = new HashMap<>();
                if (validateItem()) {
                    try{
                        int productKey = Integer.parseInt(viewModel.getProductKey().getValue());
                        productAdd.put(Integer.toString(productKey), new Product(edtCode.getText().toString(), edtName.getText().toString(), edtDescription.getText().toString(),
                                Long.parseLong(edtPrice.getText().toString().substring(1)), Long.parseLong(edtTotalPrice.getText().toString().substring(1)), Long.parseLong(edtQuantity.getText().toString()), tvDate.getText().toString()));
                        productCategory.updateChildren(productAdd);
                        productAdd.put(Integer.toString(productKey),  new Product(edtCode.getText().toString(), edtName.getText().toString(), edtDescription.getText().toString(),
                                Long.parseLong(edtPrice.getText().toString().substring(1)), Long.parseLong(edtQuantity.getText().toString()), Long.parseLong(edtTotalPrice.getText().toString().substring(1)), tvDate.getText().toString(), viewModel.getCategoryCode().getValue()));
                        product.updateChildren(productAdd);
                        if (uploadImage()){
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    viewModel.setProductChanged("true");
                                    navController.navigate(R.id.action_Show_to_Products);
                                }
                            }, 1000);
                        }else{
                            navController.navigate(R.id.action_Show_to_Products);
                        }



                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

        });

        imvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(v.getContext());
                View view = getLayoutInflater().inflate(R.layout.delete_popup, null, false);
                btnCancel = view.findViewById(R.id.btn_deletePopup_Cancel);
                btnDelete = view.findViewById(R.id.btn_deletePopup_Delete);

                builder.setView(view);
                dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.DIM_AMOUNT_CHANGED);
                dialog.show();

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference productCategory;
                        if (navController.getPreviousBackStackEntry().getDestination().toString().contains("productFragment")) {
                            productCategory = bdRef.child("productCategories/" + viewModel.getCategoryCode().getValue() + "/" + viewModel.getProductKey().getValue());
                        } else {
                            productCategory = bdRef.child("productCategories/" + viewModel.getProductCategory().getValue() + "/" + viewModel.getProductKey().getValue());
                        }
                        DatabaseReference product = bdRef.child("products/" + viewModel.getProductKey().getValue());
                        product.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().removeValue();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        productCategory.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().removeValue();

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        if (navController.getPreviousBackStackEntry().getDestination().toString().contains("productFragment")){
                            navController.navigate(R.id.action_Show_to_Products);
                        }else{
                            navController.navigate(R.id.action_Show_to_Search);
                        }
                        dialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        imvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnSave.getVisibility() == View.VISIBLE){
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
                            if (navController.getPreviousBackStackEntry().getDestination().toString().contains("productFragment")){
                                navController.navigate(R.id.action_Show_to_Products);
                            }else{
                                navController.navigate(R.id.action_Show_to_Search);
                            }
                            dialog.dismiss();
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }else{
                    if (navController.getPreviousBackStackEntry().getDestination().toString().contains("productFragment")){
                        navController.navigate(R.id.action_Show_to_Products);
                    }else{
                        navController.navigate(R.id.action_Show_to_Search);
                    }
                }

            }
        });

        return root;
    }
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                imagePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imagePath);
                    imvShowImage.setBackgroundColor(0x292929);
                    imvShowImage.setImageBitmap(bitmap);
                    btnSave.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "An issue occurred while loading the image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    });
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        resultLauncher.launch(Intent.createChooser(intent, "Select an image"));
    }
    private boolean uploadImage(){
        storage = FirebaseStorage.getInstance().getReference().child("Resources/Products");

        if (imagePath != null) {
            StorageReference ref = storage.child(edtCode.getText().toString() + ".jpg");
            ref.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = ((100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount());
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress((int) progress);
                }
            });
            return true;
        }else{
            return false;
        }
    }
    public void loadImage (Context context, ImageView imvImage, String code){
        String cachePath = context.getCacheDir().getAbsolutePath() + File.separator + code + ".jpg";
        imvImage.setImageBitmap(BitmapFactory.decodeFile(cachePath));

    }
    public boolean validateItem() {
        String code = edtCode.getText().toString();
        String name = edtName.getText().toString();
        String description = edtDescription.getText().toString();
        String price = edtPrice.getText().toString();
        String quantity = edtQuantity.getText().toString();
        String date = tvDate.getText().toString();
        Log.e("TAF1", date);

        if (validateCode(code) & validateName(name) & validateDescription(description) & validatePrice(price) & validateQuantity(quantity) & validateExpiration(date)) {
            return true;
        } else {
            return false;
        }
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
    private boolean validateExpiration(String date) {
        if (date.isEmpty()) {
            tvDate.setError(getText(R.string.emptyField));
            return false;
        } else {
            tvDate.setError(null);
            return true;
        }
    }

}