package com.sc703.gualmarsh.principal.inventory;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class InventoryFragment extends Fragment {

    private final FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference bdRef = fDatabase.getReference();
    private final DatabaseReference categoryDB = bdRef.child("categories");
    private CategoryAdapter categoryAdapter;
    private GridLayoutManager gridLayoutManager;
    private ItemViewModel viewModel;
    boolean defaultView = true;
    private String category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inventory, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        FirebaseRecyclerOptions<Category> options
                = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(categoryDB, Category.class)
                .build();
        categoryAdapter = new CategoryAdapter(options, gridLayoutManager);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        ImageView imvChangeView = root.findViewById(R.id.imv_change_view);

        imvChangeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(defaultView ==true){
                    imvChangeView.setImageResource(R.drawable.ic_sort_down);
                    defaultView = false;
                }else{
                    imvChangeView.setImageResource(R.drawable.ic_sort_list);
                    defaultView = true;
                }
                switchLayout();

            }
        });

        recyclerView.setAdapter(categoryAdapter);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
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
    private void switchLayout(){
        if (gridLayoutManager.getSpanCount() == 1){
            gridLayoutManager.setSpanCount(2);
        }else{
            gridLayoutManager.setSpanCount(1);
        }
        categoryAdapter.notifyItemRangeChanged(0,2);
    }
}