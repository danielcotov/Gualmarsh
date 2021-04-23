package com.sc703.gualmarsh.principal.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
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
import com.sc703.gualmarsh.database.models.search.SearchAdapter;
import com.sc703.gualmarsh.database.models.product.Product;
import com.sc703.gualmarsh.principal.inventory.ItemClickListener;
import com.sc703.gualmarsh.principal.inventory.ItemViewModel;


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
        searchAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                searchAdapter.getRef(position).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        try {
                            if (snapshot.getValue() != null) {
                                try {
                                    viewModel.setProductKey(snapshot.getKey());
                                    viewModel.setProductCode(snapshot.child("code").getValue().toString());
                                    viewModel.setProductName(snapshot.child("name").getValue().toString());
                                    viewModel.setProductDescription(snapshot.child("description").getValue().toString());
                                    viewModel.setProductPrice("¢" + snapshot.child("price").getValue().toString());
                                    viewModel.setProductQuantity(snapshot.child("quantity").getValue().toString());
                                    viewModel.setProductCategory(snapshot.child("category").getValue().toString());

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
                final Handler handler = new Handler();
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_principal_fragment);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navController.navigate(R.id.action_Search_to_Show);
                    }
                }, 10);

            }
        });
        searchAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        try{
            searchAdapter.stopListening();
        } catch (NullPointerException e){
            e.printStackTrace();
        }

    }

}