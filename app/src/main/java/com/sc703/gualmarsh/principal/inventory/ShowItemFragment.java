package com.sc703.gualmarsh.principal.inventory;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.database.models.product.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
    LinearLayout btn_export;

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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference productCategory;
                if (viewModel.getCategoryCode().getValue() != null){
                    productCategory = bdRef.child("productCategories/" + viewModel.getCategoryCode().getValue());
                }else{
                    productCategory = bdRef.child("productCategories/" + viewModel.getProductCategory().getValue());
                }
                DatabaseReference product = bdRef.child("products/");
                Map<String, Object> productAdd = new HashMap<>();
                if (addItem(v)) {
                    try{
                        Log.e("TAG2", viewModel.getProductKey().getValue());
                        int productKey = Integer.parseInt(viewModel.getProductKey().getValue());
                        productAdd.put(Integer.toString(productKey), new Product(edtCode.getText().toString(), edtName.getText().toString(), edtDescription.getText().toString(),
                                Long.parseLong(edtPrice.getText().toString().substring(1)), Long.parseLong(edtQuantity.getText().toString())));
                        productCategory.updateChildren(productAdd);
                        productAdd.put(Integer.toString(productKey),  new Product(edtCode.getText().toString(), edtName.getText().toString(), edtDescription.getText().toString(),
                                Long.parseLong(edtPrice.getText().toString().substring(1)), Long.parseLong(edtQuantity.getText().toString()), viewModel.getCategoryCode().getValue()));
                        product.updateChildren(productAdd);
                        //uploadImage(v);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

        });

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
        imvImage.setImageBitmap(BitmapFactory.decodeFile(cachePath));

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