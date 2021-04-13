package com.sc703.gualmarsh.principal.inventory;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sc703.gualmarsh.R;

public class ShowItemFragment extends Fragment {

    private ImageView imvClose;
    private EditText edtName, edtQuantity, edtCode, edtDescription, edtPrice;
    String bName, bQuantity, bCode, bDescription, bPrice;
    Button btnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_show_item, container, false);

        edtName = root.findViewById(R.id.edt_showItem_productName);
        edtCode = root.findViewById(R.id.edt_showItem_barcode);
        edtQuantity = root.findViewById(R.id.edt_showItem_quantity);
        edtDescription = root.findViewById(R.id.edt_showItem_description);
        edtPrice = root.findViewById(R.id.edt_showItem_price);
        imvClose = root.findViewById(R.id.showItem_Close);
        btnSave = root.findViewById(R.id.btn_showItem_Save);

        bName = edtName.getText().toString();

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(bName)){
                    System.out.println("MALDTA SEA");
                    btnSave.setVisibility(View.VISIBLE);
                }else{
                    System.out.println("JAJAJA");
                    btnSave.setVisibility(View.GONE);
                }
                System.out.println(s);
                System.out.println(bName);
            }

            @Override
            public void afterTextChanged(Editable s) {

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