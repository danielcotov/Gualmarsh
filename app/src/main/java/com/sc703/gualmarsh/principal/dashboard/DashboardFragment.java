package com.sc703.gualmarsh.principal.dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.FileUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.*;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class DashboardFragment extends Fragment {
    TextView itemsNum, categoriesNum, dashboardQuantity, dashboardTotal;
    private FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference bdRef = fDatabase.getReference();
    private DatabaseReference categoriesRef = bdRef.child("categories");
    private DatabaseReference allProducts = bdRef.child("products");
    private Long count = Long.parseLong("0");
    private Long counter = Long.parseLong("0");
    private ItemViewModel viewModel;
    private StringBuilder data = new StringBuilder();
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
        ImageView exportButton = root.findViewById(R.id.btn_export);

        if(Build.VERSION.SDK_INT >= 21){
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
                exportDB();
            }
        });

        return root;
    }

    public void loadWidgets(Long count){
        categoriesRef.addListenerForSingleValueEvent (new ValueEventListener() {
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
            allProducts.child(Integer.toString(i)).child("quantity").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try{
                        Log.e("TAG",snapshot.getValue().toString());
                        sum += Integer.parseInt(snapshot.getValue().toString());
                        dashboardQuantity.setText(String.valueOf(sum));
                        itemsNum.setText(String.valueOf(count));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            allProducts.child(Integer.toString(i)).child("price").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   try{
                       sum2+= Integer.parseInt(snapshot.getValue().toString());
                       dashboardTotal.setText(String.valueOf(sum2));
                   }catch (Exception e){
                       e.printStackTrace();
                   }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }
    public void exportDB(){
        data.append("Category,ID,Code,Quantity,Price,Name,Description");
        bdRef.child("productCategories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    count++;
                    int i = 0;
                    for(DataSnapshot catds: ds.getChildren()){
                        i++;
                        data.append("\n");
                        data.append(ds.getKey());
                        data.append(",");
                            data.append(catds.child("code").getValue().toString()).append(",").
                                    append(catds.child("quantity").getValue().toString()).append(",").
                                    append(catds.child("price").getValue().toString()).append(",").
                                    append(catds.child("name").getValue().toString()).append(",").
                                    append(catds.child("description").getValue().toString());
                    }

                }
                try{
                    FileOutputStream out = getContext().openFileOutput("data.csv", Context.MODE_PRIVATE);
                    out.write((data.toString()).getBytes());
                    out.close();

                    Context context = getContext();
                    File fileLocation = new File(getContext().getFilesDir(), "dbdata.csv");
                    Uri path = FileProvider.getUriForFile(context, "com.sc703.gualmarsh.FileProvider", fileLocation);
                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                    fileIntent.setType("text/csv");
                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, "DBBackup");
                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                    getContext().startActivity(Intent.createChooser(fileIntent, "Open with"));

                }catch(Exception e){
                    e.printStackTrace();
                }
                Log.e("DATA:",data.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}