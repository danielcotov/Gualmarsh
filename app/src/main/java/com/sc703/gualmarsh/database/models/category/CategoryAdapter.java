package com.sc703.gualmarsh.database.models.category;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.database.models.product.Product;
import com.sc703.gualmarsh.principal.items.ItemClickListener;

public class CategoryAdapter extends FirebaseRecyclerAdapter<Category, CategoryAdapter.Holder>  {

    private ItemClickListener itemClickListener;

    public CategoryAdapter(@NonNull FirebaseRecyclerOptions<Category> options)
    {
        super(options);
    }

    public static class Holder extends RecyclerView.ViewHolder{
        TextView tvCategoryCode, tvCategoryName, tvCategoryQuantity;

        public Holder(View item){
            super(item);
            tvCategoryCode = item.findViewById(R.id.tv_grid_code);
            tvCategoryName = item.findViewById(R.id.tv_grid_name);
            tvCategoryQuantity = item.findViewById(R.id.tv_grid_quantity);
        }
    }

    @NonNull
    @Override
    public CategoryAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_view, parent, false);
        return new Holder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.Holder holder, int position, @NonNull Category model) {

        holder.tvCategoryCode.setText(model.getCode());
        holder.tvCategoryName.setText(String.format(String.valueOf(model.getName())));
        if (model.getQuantity() != null) {
            holder.tvCategoryQuantity.setText(model.getQuantity().toString());
        }else{
            holder.tvCategoryQuantity.setText("");
        }



    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;

    }


}
