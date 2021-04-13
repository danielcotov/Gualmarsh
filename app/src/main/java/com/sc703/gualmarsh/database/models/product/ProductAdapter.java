package com.sc703.gualmarsh.database.models.product;


import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.principal.PrincipalActivity;
import com.sc703.gualmarsh.principal.inventory.ItemClickListener;

import java.io.File;
import java.io.IOException;

public class ProductAdapter extends FirebaseRecyclerAdapter<Product, ProductAdapter.Holder>  {

    private ItemClickListener itemClickListener;
    private GridLayoutManager gridLayoutManager;
    private StorageReference storage;



    public ProductAdapter(@NonNull FirebaseRecyclerOptions<Product> options, GridLayoutManager gridLayoutManager)
    {
        super(options);
        this.gridLayoutManager = gridLayoutManager;
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
        TextView tvProductCode, tvProductName, tvProductQuantity;
        ImageView imvImage;

        public Holder(View item, int viewType){
            super(item);
            if (viewType != 1) {
                imvImage = item.findViewById(R.id.imv_gridItemPhoto);
                tvProductCode = item.findViewById(R.id.tv_grid_code);
                tvProductName = item.findViewById(R.id.tv_grid_name);
                tvProductQuantity = item.findViewById(R.id.tv_grid_quantity);
            }else{
                imvImage = item.findViewById(R.id.imv_list_itemPhoto);
                tvProductCode = item.findViewById(R.id.tv_list_code);
                tvProductName = item.findViewById(R.id.tv_list_name);
                tvProductQuantity = item.findViewById(R.id.tv_list_quantity);
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
        loadImages(holder.imvImage.getContext(), holder.imvImage, model.getCode());
        holder.tvProductCode.setText(model.getCode());
        holder.tvProductName.setText(String.format(String.valueOf(model.getName())));
        if (model.getQuantity() != null) {
            holder.tvProductQuantity.setText(model.getQuantity().toString());
        }else{
            holder.tvProductQuantity.setText("");
        }

    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;

    }
    public void loadImages (Context context, ImageView imvImage, String code){
        String cachePath = context.getCacheDir().getAbsolutePath() + File.separator + code + ".jpg";
        File cacheFile = new File(cachePath);
        if (!cacheFile.exists()){
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
