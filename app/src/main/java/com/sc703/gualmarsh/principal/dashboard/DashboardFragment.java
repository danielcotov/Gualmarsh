package com.sc703.gualmarsh.principal.dashboard;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class DashboardFragment extends Fragment {
    TextView itemsNum;
    TextView categoriesNum;
    TextView dashboardQuantity;
    TextView dashboardTotal;
    private FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
    private DatabaseReference BDref = FBDB.getReference();
    private DatabaseReference categoriesref = BDref.child("categories");
    private DatabaseReference allRefs = FirebaseDatabase.getInstance().getReference();

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


        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        categoriesref.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String textCategories = String.valueOf(snapshot.getChildrenCount());
//                dashboardQuantity.setText(String.valueOf(snapshot.getChildrenCount()));

                categoriesNum.setText(textCategories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String text = "Unable to establish a connection to the DB";
                categoriesNum.setText(text);
            }
        });
        allRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = 0;
                long productSnapshotPrice = 0;

                DataSnapshot productCattSnapshot = snapshot.child("productCategories");
                DataSnapshot productSnapshot = snapshot.child("products");
                count = count + productCattSnapshot.getChildrenCount();
                count = count + productSnapshot.getChildrenCount();
                dashboardQuantity.setText(String.valueOf(count));

//                productSnapshotPrice = productSnapshotPrice + snapshot.child("products").child("1").getValue(Long.class );
//                productSnapshotPrice = productSnapshotPrice + (long) snapshot.child("productCategories").child("price").getValue();
//                dashboardTotal.setText(snapshot.child("products").child("1").child("price").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}