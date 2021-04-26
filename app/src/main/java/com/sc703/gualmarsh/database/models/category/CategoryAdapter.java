package com.sc703.gualmarsh.database.models.category;


import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
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

public class CategoryAdapter extends FirebaseRecyclerAdapter<Category, CategoryAdapter.Holder> {
    private ItemClickListener itemClickListener;
    private GridLayoutManager gridLayoutManager;
    private StorageReference storage;
    private final FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference bdRef = fDatabase.getReference();
    private ItemViewModel viewModel;


    public CategoryAdapter(@NonNull FirebaseRecyclerOptions<Category> options, GridLayoutManager gridLayoutManager) {
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

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvCategoryCode, tvCategoryName, tvCategoryQuantity;
        ImageView imvImage, imvExport;

        public Holder(View item, int viewType) {
            super(item);
            if (viewType != 1) {
                imvImage = item.findViewById(R.id.imv_gridItemPhoto);
                tvCategoryCode = item.findViewById(R.id.tv_grid_code);
                tvCategoryName = item.findViewById(R.id.tv_grid_name);
                tvCategoryQuantity = item.findViewById(R.id.tv_grid_quantity);

            } else {
                imvImage = item.findViewById(R.id.imv_list_itemPhoto);
                tvCategoryCode = item.findViewById(R.id.tv_list_code);
                tvCategoryName = item.findViewById(R.id.tv_list_name);
                tvCategoryQuantity = item.findViewById(R.id.tv_list_quantity);
                imvExport = item.findViewById(R.id.list_more);
            }
            itemView.setOnClickListener(new OnClickListener() {
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
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view, parent, false), viewType);
        } else {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_view, parent, false), viewType);
        }

    }




    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.Holder holder, int position, @NonNull Category model) {
        loadImages(holder.imvImage.getContext(), holder.imvImage, model.getCode());
        holder.tvCategoryCode.setText(model.getCode());
        holder.tvCategoryName.setText(String.format(String.valueOf(model.getName())));
        if (model.getQuantity() != null) {
            holder.tvCategoryQuantity.setText(model.getQuantity().toString());
        } else {
            holder.tvCategoryQuantity.setText("");
        }


        try {
            holder.imvExport.setOnClickListener(new OnClickListener() {
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
                                bdRef.child("categories").child(Integer.toString(position+1)).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        bdRef.child("productCategories").child(snapshot.child("code").getValue().toString()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                StringBuilder data = new StringBuilder();
                                                data.append("Product Name,Quantity,Barcode,Unit Price,Description");
                                                int j=1;
                                                for(int i=1; i<=dataSnapshot.getChildrenCount(); j++){
                                                    try{
                                                        data.append("\n").append(dataSnapshot.child(Integer.toString(j)).child("name").getValue().toString()).append(",").
                                                                append(dataSnapshot.child(Integer.toString(j)).child("quantity").getValue().toString()).append(",").
                                                                append(dataSnapshot.child(Integer.toString(j)).child("code").getValue().toString()).append(",").
                                                                append(dataSnapshot.child(Integer.toString(j)).child("unitPrice").getValue().toString()).append(",").
                                                                append(dataSnapshot.child(Integer.toString(j)).child("description").getValue().toString());
                                                        i++;
                                                    }catch (Exception e){

                                                    }
                                                }

                                                try{
                                                    FileOutputStream out = v.getContext().openFileOutput("data.csv", Context.MODE_PRIVATE);
                                                    out.write((data.toString()).getBytes());
                                                    out.close();

                                                    Context context = v.getContext();
                                                    File fileLocation = new File(v.getContext().getFilesDir(), "data.csv");
                                                    Uri path = FileProvider.getUriForFile(context, "com.sc703.gualmarsh.FileProvider", fileLocation);
                                                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                                                    fileIntent.setType("text/*");
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

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void loadImages(Context context, ImageView imvImage, String code) {
        String cachePath = context.getCacheDir().getAbsolutePath() + File.separator + code + ".jpg";
        File cacheFile = new File(cachePath);
        if (!cacheFile.exists()) {
            storage = FirebaseStorage.getInstance().getReference().child("Resources/Categories/" + code + ".jpg");
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
        } else {
            imvImage.setImageBitmap(BitmapFactory.decodeFile(cachePath));
        }
    }
}
