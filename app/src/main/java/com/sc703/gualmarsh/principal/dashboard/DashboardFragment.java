package com.sc703.gualmarsh.principal.dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.database.models.itemView.ItemViewModel;
import com.sc703.gualmarsh.database.models.product.Product;


import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

public class DashboardFragment extends Fragment {
    private TextView itemsNum, categoriesNum, dashboardQuantity, dashboardTotal;
    private FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference bdRef = fDatabase.getReference();
    private DatabaseReference categories = bdRef.child("categories");
    private DatabaseReference products = bdRef.child("products");
    private Long count = Long.parseLong("0");
    private StringBuilder data = new StringBuilder();
    private int totalQuantity, totalPrice;
    private ItemViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        itemsNum = root.findViewById(R.id.tv_dashboardItemsNum);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        categoriesNum = root.findViewById(R.id.tv_dashboardCategoriesNum);
        dashboardQuantity = root.findViewById(R.id.tv_dashboardQuantityNum);
        dashboardTotal = root.findViewById(R.id.tv_dashboardTotalNum);
        ImageView exportButton = root.findViewById(R.id.btn_export);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.w_darkBG));
        }

        bdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = snapshot.child("products").getChildrenCount();
                loadWidgets(count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportProducts();
            }
        });

        return root;
    }

    public void loadWidgets(Long count) {
        categories.addListenerForSingleValueEvent(new ValueEventListener() {
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
        products.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    try{
                        totalQuantity += Integer.parseInt(dataSnapshot.child("quantity").getValue().toString());
                        dashboardQuantity.setText(String.valueOf(totalQuantity));
                        totalPrice += Integer.parseInt(dataSnapshot.child("totalPrice").getValue().toString());
                        dashboardTotal.setText("¢" + Integer.toString(totalPrice));
                        itemsNum.setText(String.valueOf(count));
                        viewModel.setProductKey(dataSnapshot.getRef().getKey());

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
         }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void exportProducts() {
        data.append("Category ID,Barcode,Name,Description,Unit Price,Quantity,Total Price,Expiration Date,Last Updated");
        bdRef.child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    data.append("\n").append(dataSnapshot.child("category").getValue().toString()).append(",").append(dataSnapshot.child("code").getValue().toString()).
                            append(",").append(dataSnapshot.child("name").getValue().toString()).append(",").append(dataSnapshot.child("description").getValue().toString()).
                            append(",").append(dataSnapshot.child("unitPrice").getValue().toString()).append(",").append(dataSnapshot.child("quantity").getValue().toString()).
                            append(",").append(dataSnapshot.child("totalPrice").getValue().toString()).append(",").append(dataSnapshot.child("expiration").getValue().toString()).
                            append(",").append(dataSnapshot.child("lastUpdated").getValue().toString());
                }
                try {
                    FileOutputStream out = getContext().openFileOutput("data.csv", Context.MODE_PRIVATE);
                    out.write((data.toString()).getBytes());
                    out.close();

                    Context context = getContext();
                    File fileLocation = new File(getContext().getFilesDir(), "data.csv");
                    Uri path = FileProvider.getUriForFile(context, "com.sc703.gualmarsh.FileProvider", fileLocation);
                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                    fileIntent.setType("text/csv");
                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Product Data Export");
                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);

                    getContext().startActivity(Intent.createChooser(fileIntent, "Open with"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("DATA:", data.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        data.setLength(0);
    }


}