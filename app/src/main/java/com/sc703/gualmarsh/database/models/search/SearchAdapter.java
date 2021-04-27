package com.sc703.gualmarsh.database.models.search;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.database.models.product.Product;
import com.sc703.gualmarsh.principal.inventory.ItemClickListener;

import java.io.File;

public class SearchAdapter extends FirebaseRecyclerAdapter<Product, SearchAdapter.Holder> {

    private ItemClickListener itemClickListener;
    private StorageReference storage;

    public SearchAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {
        super(options);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvProductCode, tvProductName, tvProductQuantity, tvProductUnitPrice;
        ImageView imvImage;

        public Holder(View item, int viewType) {
            super(item);
            imvImage = item.findViewById(R.id.imv_list_itemPhoto);
            tvProductCode = item.findViewById(R.id.tv_list_code);
            tvProductName = item.findViewById(R.id.tv_list_name);
            tvProductQuantity = item.findViewById(R.id.tv_list_quantity);
            tvProductUnitPrice = item.findViewById(R.id.tv_list_price);


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
    public SearchAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchAdapter.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_search_view, parent, false), viewType);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.Holder holder, int position, @NonNull Product model) {
        loadImages(holder.imvImage.getContext(), holder.imvImage, model.getCode());
        holder.tvProductCode.setText(model.getCode());
        holder.tvProductName.setText(String.valueOf(model.getName()));
        if (model.getQuantity() != null) {
            holder.tvProductQuantity.setText(model.getQuantity().toString());
        } else {
            holder.tvProductQuantity.setText("");
        }
        if (model.getUnitPrice() != null) {
            holder.tvProductUnitPrice.setText("¢" + model.getUnitPrice().toString());
        } else {
            holder.tvProductUnitPrice.setText("");
        }

    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }

    public void loadImages(Context context, ImageView imvImage, String code) {
        String cachePath = context.getCacheDir().getAbsolutePath() + File.separator + code + ".png";
        File cacheFile = new File(cachePath);
        if (!cacheFile.exists()) {
            storage = FirebaseStorage.getInstance().getReference().child("Resources/Products/" + code + ".png");
            File localFile = null;
            try {
                localFile = new File(context.getCacheDir(), code + ".png");
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
        } else {
            imvImage.setImageBitmap(BitmapFactory.decodeFile(cachePath));
        }
    }
}
