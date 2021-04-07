package com.sc703.gualmarsh.principal.inventory;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.database.models.product.ProductAdapter;
import com.sc703.gualmarsh.database.models.product.Product;

public class ProductFragment extends Fragment {

    private final FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference bdRef = fDatabase.getReference();
    private ProductAdapter productAdapter;
    private ItemViewModel viewModel;
    private GridLayoutManager gridLayoutManager;
    public boolean defaultView = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inventory_product, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        DatabaseReference product = bdRef.child("productCategories/" + viewModel.getCategoryCode().getValue());
        FirebaseRecyclerOptions<Product> options
                = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(product, Product.class)
                .build();
        productAdapter = new ProductAdapter(options, gridLayoutManager);
        recyclerView.setAdapter(productAdapter);
        ImageView imvChangeView = root.findViewById(R.id.imv_change_view_inventory_product);
        imvChangeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(defaultView == true){
                    imvChangeView.setImageResource(R.drawable.ic_sort_grid);
                    defaultView = false;
                }else{
                    imvChangeView.setImageResource(R.drawable.ic_sort_list);
                    defaultView = true;
                }
                switchLayout();

            }
        });
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        productAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                productAdapter.getRef(position).child("code").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        try {
                            if (snapshot.getValue() != null) {
                                try {
                                    viewModel.setProductCode(snapshot.getValue().toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("TAG", " it's null.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("onCancelled", " cancelled");
                    }
                });;

                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_principal_fragment);
                navController.navigate(R.id.action_Products_to_Show);
            }
        });
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
    private void switchLayout(){
        if (gridLayoutManager.getSpanCount() == 1){
            gridLayoutManager.setSpanCount(2);
        }else{
            gridLayoutManager.setSpanCount(1);
        }
        productAdapter.notifyItemRangeChanged(0,2);
    }
}