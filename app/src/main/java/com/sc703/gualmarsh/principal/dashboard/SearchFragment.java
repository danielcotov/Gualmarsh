package com.sc703.gualmarsh.principal.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.database.models.SearchAdapter;
import com.sc703.gualmarsh.database.models.product.Product;
import com.sc703.gualmarsh.database.models.product.ProductAdapter;
import com.sc703.gualmarsh.principal.inventory.ItemViewModel;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private final FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference bdRef = fDatabase.getReference();
    private SearchAdapter searchAdapter;
    private ItemViewModel viewModel;
    private EditText edtSearchBar;
    private LinearLayout logoSearch;
    private String search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        recyclerView = root.findViewById(R.id.recyclerviewSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        System.out.println(viewModel.getCategoryCode().getValue());
        DatabaseReference product = bdRef.child("productCategories/" + viewModel.getCategoryCode().getValue());
        FirebaseRecyclerOptions<Product> options
                = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(product, Product.class)
                .build();
        searchAdapter = new SearchAdapter(options);
        recyclerView.setAdapter(searchAdapter);
        logoSearch = root.findViewById(R.id.imv_logoSearch);
        search = edtSearchBar.getText().toString();

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(search.isEmpty()){
                    logoSearch.setVisibility(View.GONE);
                }else{
                    logoSearch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        searchAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        searchAdapter.stopListening();
    }
}