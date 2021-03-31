package com.sc703.gualmarsh.database.models.product;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.principal.items.ItemClickListener;

public class ProductAdapter extends FirebaseRecyclerAdapter<Product, ProductAdapter.Holder>  {

    private ItemClickListener itemClickListener;

    public ProductAdapter(@NonNull FirebaseRecyclerOptions<Product> options)
    {
        super(options);
    }

    public static class Holder extends RecyclerView.ViewHolder{
        TextView tvProductCode, tvProductName, tvProductQuantity;

        public Holder(View item){
            super(item);
            tvProductCode = item.findViewById(R.id.tv_grid_code);
            tvProductName = item.findViewById(R.id.tv_grid_name);
            tvProductQuantity = item.findViewById(R.id.tv_grid_quantity);
        }
    }

    @NonNull
    @Override
    public ProductAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_view, parent, false);
        return new Holder(itemRow);
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
