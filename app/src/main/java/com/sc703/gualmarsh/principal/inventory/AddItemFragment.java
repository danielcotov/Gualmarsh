package com.sc703.gualmarsh.principal.inventory;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.database.models.product.Product;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class AddItemFragment extends Fragment {
    private final FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference bdRef = fDatabase.getReference();
    private ItemViewModel viewModel;
    private EditText edtName, edtCode, edtQuantity, edtDescription, edtPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_item, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        edtName = root.findViewById(R.id.edt_addItem_productName);
        edtCode = root.findViewById(R.id.edt_addItem_barcode);
        edtQuantity = root.findViewById(R.id.edt_addItem_quantity);
        edtDescription = root.findViewById(R.id.edt_addItem_description);
        edtPrice = root.findViewById(R.id.edt_addItem_price);

        ImageView imvClose = root.findViewById(R.id.addItem_Close);
        imvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_principal_fragment);
                navController.navigate(R.id.action_Add_to_Products);
            }
        });
        Button btnSave = root.findViewById(R.id.btn_addItem_Save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference product = bdRef.child("productCategories/" + viewModel.getCategoryCode().getValue());
                Map<String, Object> productAdd = new HashMap<>();
                if (addItem(v)){
                    int productKey = Integer.parseInt(viewModel.getProductCount().getValue()) + 1;
                    productAdd.put(Integer.toString(productKey), new Product(edtCode.getText().toString(), edtName.getText().toString(), edtDescription.getText().toString(),
                            Long.parseLong(edtPrice.getText().toString()), Long.parseLong(edtQuantity.getText().toString())));
                    product.updateChildren(productAdd);
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT);
                }else{

                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT);
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
}