package com.sc703.gualmarsh.principal.inventory;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sc703.gualmarsh.R;

import java.io.File;
import java.util.Calendar;

public class ShowItemFragment extends Fragment {

    private final FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference bdRef = fDatabase.getReference();
    private ItemViewModel viewModel;
    private ImageView imvShowImage, imvClose;
    private EditText edtName, edtQuantity, edtCode, edtDescription, edtPrice;
    private String bName, bQuantity, bCode, bDescription, bPrice;
    private Button btnSave;
    private StorageReference storage;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TextView tvDate;
    private Button btnCancel, btnDiscard;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    public ShowItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_show_item, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        imvShowImage = root.findViewById(R.id.showItem_Photo);
        edtName = root.findViewById(R.id.edt_showItem_productName);
        edtCode = root.findViewById(R.id.edt_showItem_barcode);
        edtQuantity = root.findViewById(R.id.edt_showItem_quantity);
        edtDescription = root.findViewById(R.id.edt_showItem_description);
        edtPrice = root.findViewById(R.id.edt_showItem_price);
        imvClose = root.findViewById(R.id.showItem_Close);
        btnSave = root.findViewById(R.id.btn_showItem_Save);
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
        loadImage(getContext(), imvShowImage, viewModel.getProductCode().getValue());


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

        imvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_principal_fragment);
                navController.navigate(R.id.action_Show_to_Products);
            }
        });

        return root;
    }
    public void loadImage (Context context, ImageView imvImage, String code){
        String cachePath = context.getCacheDir().getAbsolutePath() + File.separator + code + ".jpg";
        File cacheFile = new File(cachePath);
        if (!cacheFile.exists()){
            storage = FirebaseStorage.getInstance().getReference().child("Resources/Products/"+ code + ".jpg");
            File localFile = null;
            try {
                localFile = new File(context.getCacheDir(), code + ".jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
            final File FINAL_FILE = localFile;
            storage.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    String file = FINAL_FILE.getAbsolutePath();
                    imvImage.setImageBitmap(BitmapFactory.decodeFile(file));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }else{
            imvImage.setImageBitmap(BitmapFactory.decodeFile(cachePath));
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

}