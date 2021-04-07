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
import android.widget.ImageView;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_item, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
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

                int productKey = Integer.parseInt(viewModel.getProductCount().getValue()) + 1;
                productAdd.put(Integer.toString(productKey), new Product("PRD03", "SOAP1", "Description", Long.parseLong("1200"), Long.parseLong("10")));
                product.updateChildren(productAdd);

            }
        });


        return root;
    }
}