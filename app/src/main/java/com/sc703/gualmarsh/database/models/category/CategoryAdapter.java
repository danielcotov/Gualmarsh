package com.sc703.gualmarsh.database.models.category;


import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.principal.inventory.InventoryFragment;
import com.sc703.gualmarsh.principal.inventory.ItemClickListener;
import com.sc703.gualmarsh.principal.inventory.ItemViewModel;

public class CategoryAdapter extends FirebaseRecyclerAdapter<Category, CategoryAdapter.Holder> {
    private ItemClickListener itemClickListener;
    private GridLayoutManager gridLayoutManager;


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

        public Holder(View item, int viewType) {
            super(item);
            if (viewType != 1) {
                tvCategoryCode = item.findViewById(R.id.tv_grid_code);
                tvCategoryName = item.findViewById(R.id.tv_grid_name);
                tvCategoryQuantity = item.findViewById(R.id.tv_grid_quantity);
            } else {
                tvCategoryCode = item.findViewById(R.id.tv_list_code);
                tvCategoryName = item.findViewById(R.id.tv_list_name);
                tvCategoryQuantity = item.findViewById(R.id.tv_list_quantity);
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
        holder.tvCategoryCode.setText(model.getCode());
        holder.tvCategoryName.setText(String.format(String.valueOf(model.getName())));
        if (model.getQuantity() != null) {
            holder.tvCategoryQuantity.setText(model.getQuantity().toString());
        } else {
            holder.tvCategoryQuantity.setText("");
        }
    }
    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }


}
