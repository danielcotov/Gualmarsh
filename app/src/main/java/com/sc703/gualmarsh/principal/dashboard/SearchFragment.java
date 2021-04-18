package com.sc703.gualmarsh.principal.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.database.models.SearchAdapter;
import com.sc703.gualmarsh.database.models.category.Category;
import com.sc703.gualmarsh.database.models.product.Product;
import com.sc703.gualmarsh.database.models.product.ProductAdapter;
import com.sc703.gualmarsh.principal.inventory.ItemViewModel;

import java.sql.SQLOutput;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private final FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference bdRef = fDatabase.getReference();
    private SearchAdapter searchAdapter;
    private ItemViewModel viewModel;
    private EditText edtSearchBar;
    private LinearLayout logoSearch;
    private DatabaseReference productRef = bdRef.child("products");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        logoSearch = root.findViewById(R.id.imv_logoSearch);
        recyclerView = root.findViewById(R.id.recyclerviewSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        edtSearchBar = root.findViewById(R.id.edt_search);
        edtSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()){
                    logoSearch.setVisibility(View.GONE);
                    try{
                        viewModel.setProductSearch(s.toString());
                        bdRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Long count = snapshot.child("products").getChildrenCount();
                                try{
                                    searchProduct(count, viewModel.getProductSearch().getValue());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }catch(Exception e){
                    }
                }else{
                    logoSearch.setVisibility(View.VISIBLE);
                    searchProduct(Long.parseLong("0"), "EmptySearch");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return root;
    }
    @Override
    public void onStart() {
        super.onStart();

    }
    public void searchProduct(Long count, String search) {
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        if (!search.equals("EmptySearch")) {
            for (int i = 1; i <= count; i++) {
                FirebaseRecyclerOptions<Product> options
                        = new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(productRef.orderByChild("name").startAt(search).endAt(search + "\uf899"), Product.class)
                        .build();
                searchAdapter = new SearchAdapter(options);
                recyclerView.setAdapter(searchAdapter);
            }
        } else {
            FirebaseRecyclerOptions<Product> options
                    = new FirebaseRecyclerOptions.Builder<Product>()
                    .setQuery(productRef.orderByChild("name").startAt(search).endAt(search + "\uf899"), Product.class)
                    .build();
            searchAdapter = new SearchAdapter(options);
            recyclerView.setAdapter(searchAdapter);
        }
        searchAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        searchAdapter.stopListening();
    }

}