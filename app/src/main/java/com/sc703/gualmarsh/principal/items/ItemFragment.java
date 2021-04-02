package com.sc703.gualmarsh.principal.items;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.database.models.category.Category;
import com.sc703.gualmarsh.database.models.category.CategoryAdapter;

public class ItemFragment extends Fragment {

    private final FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference bdRef = fDatabase.getReference();
    private final DatabaseReference categoryDB = bdRef.child("categories");
    private CategoryAdapter categoryAdapter;
    private ItemViewModel viewModel;

    private String category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_item, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<Category> options
                = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(categoryDB, Category.class)
                .build();
        categoryAdapter = new CategoryAdapter(options);
        recyclerView.setAdapter(categoryAdapter);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        categoryAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                categoryAdapter.getRef(position).child("code").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        try {
                            if (snapshot.getValue() != null) {
                                try {
                                    viewModel.setCode(snapshot.getValue().toString());
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
                navController.navigate(R.id.action_Items_to_Products);
            }
        });

        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        categoryAdapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        categoryAdapter.stopListening();
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}