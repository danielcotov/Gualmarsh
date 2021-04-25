package com.sc703.gualmarsh.database.models.product;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.principal.inventory.ItemClickListener;
import com.sc703.gualmarsh.database.models.itemView.ItemViewModel;

import java.io.File;
import java.io.FileOutputStream;

public class ProductAdapter extends FirebaseRecyclerAdapter<Product, ProductAdapter.Holder>  {

    private ItemClickListener itemClickListener;
    private GridLayoutManager gridLayoutManager;
    private StorageReference storage;
    private final FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference bdRef = fDatabase.getReference();
    private Activity activity;
    private ItemViewModel viewModel;
    private Context currentContext;
    private Fragment currentFragment;

    public ProductAdapter(@NonNull FirebaseRecyclerOptions<Product> options, GridLayoutManager gridLayoutManager, Activity activity, Fragment currentFragment)
    {
        super(options);
        this.gridLayoutManager = gridLayoutManager;
        this.activity = activity;
        this.currentFragment = currentFragment;
    }


    @Override
    public int getItemViewType(int position) {
        int spanCount = gridLayoutManager.getSpanCount();
        if (spanCount == 1) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView tvProductCode, tvProductName, tvProductQuantity, tvProductPrice;
        ImageView imvImage, imvExport;

        public Holder(View item, int viewType){
            super(item);
            if (viewType != 1) {
                imvImage = item.findViewById(R.id.imv_gridItemPhoto);
                tvProductCode = item.findViewById(R.id.tv_grid_code);
                tvProductName = item.findViewById(R.id.tv_grid_name);
                tvProductQuantity = item.findViewById(R.id.tv_grid_quantity);
                tvProductPrice = item.findViewById(R.id.tv_grid_price);
            }else{
                imvImage = item.findViewById(R.id.imv_list_itemPhoto);
                tvProductCode = item.findViewById(R.id.tv_list_code);
                tvProductName = item.findViewById(R.id.tv_list_name);
                tvProductQuantity = item.findViewById(R.id.tv_list_quantity);
                tvProductPrice = item.findViewById(R.id.tv_list_price);
                imvExport = item.findViewById(R.id.list_more);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                        itemClickListener.OnItemClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ProductAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view, parent, false), viewType);
        }else{
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_view, parent, false), viewType);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.Holder holder, int position, @NonNull Product model) {
        loadImage(holder.imvImage.getContext(), holder.imvImage, model.getCode());
        holder.tvProductCode.setText(model.getCode());
        holder.tvProductName.setText(String.format(String.valueOf(model.getName())));
        try{
            holder.tvProductQuantity.setText(model.getQuantity().toString());
            holder.tvProductPrice.setText("¢" + model.getPrice().toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        try {
            holder.imvExport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.inflate(R.menu.item_popup_menu);
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getItemId() == R.id.popupMenu_delete){
                                Toast.makeText(v.getContext(), "DELETE", Toast.LENGTH_SHORT).show();
                            }
                            if(item.getItemId() == R.id.popupMenu_export){
                                bdRef.child("products").child(Integer.toString(position+1)).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        StringBuilder data = new StringBuilder();
                                        data.append("Product Name,Quantity,Barcode,Price,Description");
                                        try{
                                            data.append("\n").append(snapshot.child(Integer.toString(2)).child("name").getValue().toString()).append(",").
                                                    append(snapshot.child(Integer.toString(2)).child("quantity").getValue().toString()).append(",").
                                                    append(snapshot.child(Integer.toString(2)).child("code").getValue().toString()).append(",").
                                                    append(snapshot.child(Integer.toString(2)).child("price").getValue().toString()).append(",").
                                                    append(snapshot.child(Integer.toString(2)).child("description").getValue().toString());
                                        }catch (Exception e){
                                        }
                                        try{
                                            FileOutputStream out = v.getContext().openFileOutput("data.csv", Context.MODE_PRIVATE);
                                            out.write((data.toString()).getBytes());
                                            out.close();

                                            Context context = v.getContext();
                                            File fileLocation = new File(v.getContext().getFilesDir(), "data.csv");
                                            Uri path = FileProvider.getUriForFile(context, "com.sc703.gualmarsh.FileProvider", fileLocation);
                                            Intent fileIntent = new Intent(Intent.ACTION_SEND);
                                            fileIntent.setType("text/csv");
                                            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
                                            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                                            v.getContext().startActivity(Intent.createChooser(fileIntent, "Open with"));

                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            return false;
                        }
                    });
                }
            });
        } catch (Exception e) {
        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;

    }
    public void loadImage (Context context, ImageView imvImage, String code){
        NavController navController = Navigation.findNavController(activity, R.id.nav_principal_fragment);
        viewModel = new ViewModelProvider(currentFragment.requireActivity()).get(ItemViewModel.class);
        String cachePath = context.getCacheDir().getAbsolutePath() + File.separator + code + ".jpg";
        File cacheFile = new File(cachePath);

        if (!cacheFile.exists() || (navController.getPreviousBackStackEntry().getDestination().toString().contains("showItem") && viewModel.getProductChanged().getValue() != null) ){
            storage = FirebaseStorage.getInstance().getReference().child("Resources/Products/"+ code + ".jpg");
            File localFile = null;
            try {
                localFile = new File(context.getCacheDir(), code + ".jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
            final File FINAL_FILE = localFile;
            storage.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    String file = FINAL_FILE.getAbsolutePath();
                    imvImage.setImageBitmap(BitmapFactory.decodeFile(file));
                    viewModel.setProductChanged(null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }else{
            imvImage.setImageBitmap(BitmapFactory.decodeFile(cachePath));
        }
    }

}
