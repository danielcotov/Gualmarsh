package com.sc703.gualmarsh.principal.dashboard;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.login.LoginActivity;
import com.sc703.gualmarsh.principal.inventory.ItemViewModel;

import java.util.ArrayList;
import java.util.Map;

public class DashboardFragment extends Fragment {
    TextView itemsNum;
    TextView categoriesNum;
    TextView dashboardQuantity;
    TextView dashboardTotal;
    private FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
    private DatabaseReference BDref = FBDB.getReference();
    private DatabaseReference categoriesref = BDref.child("categories");
    private DatabaseReference allProducts = BDref.child("products");
    private Long count = Long.parseLong("0");
    private Long count2 = Long.parseLong("0");
    private ItemViewModel viewModel;
    private int sum;
    private int sum2;

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        itemsNum = root.findViewById(R.id.tv_dashboardItemsNum);
        categoriesNum = root.findViewById(R.id.tv_dashboardCategoriesNum);
        dashboardQuantity = root.findViewById(R.id.tv_dashboardQuantityNum);
        dashboardTotal = root.findViewById(R.id.tv_dashboardTotalNum);

        if(Build.VERSION.SDK_INT >= 21){
            Window window = getActivity().getWindow();
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.w_darkBG));
        }

        BDref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = snapshot.child("products").getChildrenCount();
                loadWidgets(count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return root;
    }
    @Override
    public void onStart() {
        super.onStart();


    }
    public void loadWidgets(Long count){
         count2 = count;
        categoriesref.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String textCategories = String.valueOf(snapshot.getChildrenCount());

                categoriesNum.setText(textCategories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String text = "Unable to establish a connection to the DB";
                categoriesNum.setText(text);
            }
        });
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        for (int i = 1; i <= count; i++){
            System.out.println(allProducts.child(Integer.toString(i)));

            allProducts.child(Integer.toString(i)).child("quantity").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    viewModel.setProductCount(snapshot.getValue().toString());
                    sum += Integer.parseInt(snapshot.getValue().toString());
                    dashboardQuantity.setText(String.valueOf(sum));
                    itemsNum.setText(String.valueOf(count));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            allProducts.child(Integer.toString(i)).child("price").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   viewModel.setProductPrice(snapshot.getValue().toString());
                   sum2+= Integer.parseInt(snapshot.getValue().toString());
                   dashboardTotal.setText(String.valueOf(sum2));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }
}