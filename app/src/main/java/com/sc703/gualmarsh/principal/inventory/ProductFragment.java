package com.sc703.gualmarsh.principal.inventory;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.database.models.product.ProductAdapter;
import com.sc703.gualmarsh.database.models.product.Product;

public class ProductFragment extends Fragment {

    private final FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference bdRef = fDatabase.getReference();
    private ProductAdapter productAdapter;
    private ItemViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inventory_product, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DatabaseReference product = bdRef.child("productCategories/" + viewModel.getCode().getValue());
        FirebaseRecyclerOptions<Product> options
                = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(product, Product.class)
                .build();
        productAdapter = new ProductAdapter(options);
        recyclerView.setAdapter(productAdapter);

        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        productAdapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        productAdapter.stopListening();
    }
}