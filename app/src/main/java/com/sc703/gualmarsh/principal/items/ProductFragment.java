package com.sc703.gualmarsh.principal.items;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

    private FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference bdRef = fDatabase.getReference();
    private DatabaseReference product = bdRef.child("products");
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_item_product, container, false);
        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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