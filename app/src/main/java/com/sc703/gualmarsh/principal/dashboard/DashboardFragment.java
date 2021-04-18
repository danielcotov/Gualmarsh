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
    private TextView itemsNum, categoriesNum, dashboardQuantity, dashboardTotal;
    private FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
    private DatabaseReference BDref = FBDB.getReference();
    private DatabaseReference categoriesref = BDref.child("categories");
    private DatabaseReference allProducts = BDref.child("products");
    private Long count = Long.parseLong("0");
    private String counter = "0";
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
        ImageView exportButton = root.findViewById(R.id.btn_export);

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
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportDB();
            }
        });

        return root;
    }

    public void loadWidgets(Long count){
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
    public void exportDB(){
        BDref.child("productCategories").child("CAT0"+ counter).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder data = new StringBuilder();
                data.append("Product Code,Description,Name,Price,Quantity");
                for(int i=1; i<=snapshot.getChildrenCount(); i++){
                    data.append("\n").append(snapshot.child(Integer.toString(i)).child("code").getValue().toString()).append(",").
                            append(snapshot.child(Integer.toString(i)).child("description").getValue().toString()).append(",").
                            append(snapshot.child(Integer.toString(i)).child("name").getValue().toString()).append(",").
                            append(snapshot.child(Integer.toString(i)).child("price").getValue().toString()).append(",").
                            append(snapshot.child(Integer.toString(i)).child("quantity").getValue().toString());

                }
                try{
                    FileOutputStream out = getContext().openFileOutput("data.csv", Context.MODE_PRIVATE);
                    out.write((data.toString()).getBytes());
                    out.close();

                    Context context = getContext();
                    File fileLocation = new File(getContext().getFilesDir(), "data.csv");
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


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}