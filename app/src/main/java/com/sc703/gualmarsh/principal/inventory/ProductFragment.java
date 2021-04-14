package com.sc703.gualmarsh.principal.inventory;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


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
    private RecyclerView recyclerView;
    private SelectionTracker<Long> selectedProductTracker;
    public boolean defaultView = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inventory_product, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        Button btnAdd = root.findViewById(R.id.btn_addItem);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        viewModel.setProductCount(Long.toString(snapshot.child("productCategories/" + viewModel.getCategoryCode().getValue()).getChildrenCount()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                bdRef.addValueEventListener(valueEventListener);
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_principal_fragment);
                navController.navigate(R.id.action_Products_to_Add);
            }
        });
        recyclerView = root.findViewById(R.id.recyclerview);
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
                if (defaultView == true) {
                    imvChangeView.setImageResource(R.drawable.ic_sort_grid);
                    defaultView = false;
                } else {
                    imvChangeView.setImageResource(R.drawable.ic_sort_list);
                    defaultView = true;
                }
                switchLayout();

            }
        });

        TextView backArrow = root.findViewById(R.id.tv_back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_principal_fragment);
                navController.navigate(R.id.action_Products_to_Inventory);
            }
        });

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        productAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                productAdapter.getRef(position).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        try {
                            if (snapshot.getValue() != null) {
                                try {
                                    viewModel.setProductCode(snapshot.child("code").getValue().toString());
                                    viewModel.setProductName(snapshot.child("name").getValue().toString());
                                    viewModel.setProductDescription(snapshot.child("description").getValue().toString());
                                    viewModel.setProductPrice("¢" + snapshot.child("price").getValue().toString());
                                    viewModel.setProductQuantity(snapshot.child("quantity").getValue().toString());

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
                });
                ;

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
    public void onStop() {
        super.onStop();
        productAdapter.stopListening();
    }

    private void switchLayout() {
        if (gridLayoutManager.getSpanCount() == 1) {
            gridLayoutManager.setSpanCount(2);
        } else {
            gridLayoutManager.setSpanCount(1);
        }
        productAdapter.notifyItemRangeChanged(0, 2);
    }


}