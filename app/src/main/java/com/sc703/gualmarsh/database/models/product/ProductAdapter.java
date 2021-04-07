package com.sc703.gualmarsh.database.models.product;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.principal.inventory.ItemClickListener;

public class ProductAdapter extends FirebaseRecyclerAdapter<Product, ProductAdapter.Holder>  {

    private ItemClickListener itemClickListener;
    private GridLayoutManager gridLayoutManager;

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
        public int position;

        public Holder(View item, int viewType){
            super(item);

            if (viewType != 1) {
                tvProductCode = item.findViewById(R.id.tv_grid_code);
                tvProductName = item.findViewById(R.id.tv_grid_name);
                tvProductQuantity = item.findViewById(R.id.tv_grid_quantity);
            }else{
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


}
