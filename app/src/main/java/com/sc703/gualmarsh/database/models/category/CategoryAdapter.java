package com.sc703.gualmarsh.database.models.category;


import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.principal.inventory.ItemClickListener;

public class CategoryAdapter extends FirebaseRecyclerAdapter<Category, CategoryAdapter.Holder>  {
    private ItemClickListener itemClickListener;


    public CategoryAdapter(@NonNull FirebaseRecyclerOptions<Category> options)
    {
        super(options);
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView tvCategoryCode, tvCategoryName, tvCategoryQuantity;

        public Holder(View item){
            super(item);
            tvCategoryCode = item.findViewById(R.id.tv_grid_code);
            tvCategoryName = item.findViewById(R.id.tv_grid_name);
            tvCategoryQuantity = item.findViewById(R.id.tv_grid_quantity);
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && itemClickListener !=null){
                        itemClickListener.OnItemClick(position);
                    }
                }
            });
        }

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
