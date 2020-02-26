package com.drei.renovarapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.drei.renovarapp.Models.Product;
import com.drei.renovarapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TherapySelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Product> productList;

    public  TherapySelectionListener selectionListener;
    public interface TherapySelectionListener{
        void onTherapySelect(Product product);
    }

    public TherapySelectionAdapter(Context context, List<Product> productList, TherapySelectionListener selectionListener) {
        this.context = context;
        this.productList = productList;
        this.selectionListener = selectionListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_therapy,parent,false);
        return new TherapySelectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TherapySelectionViewHolder selectionViewHolder = (TherapySelectionViewHolder)holder;
        selectionViewHolder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class TherapySelectionViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productName;
        private View view;

        public TherapySelectionViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            view = itemView;
        }

        public void bindData(final int position)
        {
            productName.setText(productList.get(position).getName());
            Picasso.get().load(productList.get(position).getImage()).placeholder(R.drawable.no_image_preview).into(productImage);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectionListener.onTherapySelect(productList.get(position));
                }
            });
        }
    }
}
