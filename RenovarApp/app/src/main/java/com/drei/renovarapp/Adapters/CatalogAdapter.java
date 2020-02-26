package com.drei.renovarapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.drei.renovarapp.Dialogs.ProductDialogFragment;
import com.drei.renovarapp.Models.Product;
import com.drei.renovarapp.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class CatalogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private FragmentActivity fragmentActivity;
    private Context context;
    private List<Product> productList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Product item);
    }

    public CatalogAdapter(FragmentActivity fragmentActivity, Context context, List<Product> productList) {
        this.fragmentActivity = fragmentActivity;
        this.context = context;
        this.productList = productList;
    }

    public CatalogAdapter(FragmentActivity fragmentActivity, Context context, List<Product> productList, OnItemClickListener onItemClickListener) {
        this.fragmentActivity = fragmentActivity;
        this.context = context;
        this.productList = productList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_catalog,parent,false) ;
        return new CatalogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CatalogViewHolder catalogViewHolder = (CatalogViewHolder)holder;
        catalogViewHolder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class CatalogViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView txtPrice;
        private TextView txtName;
        private View itemView;

        public CatalogViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            productImage = itemView.findViewById(R.id.productImage);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtName = itemView.findViewById(R.id.txtName);
        }

        public void bindData(final int position)
        {
            Picasso.get().load(productList.get(position).getImage())
                    .fit().centerInside()
                    .placeholder(R.drawable.no_image_preview)
                    .into(productImage);

            NumberFormat format = NumberFormat.getCurrencyInstance();
            format.setCurrency(Currency.getInstance(Locale.US));
            txtPrice.setText(format.format(productList.get(position).getPrice()));
            txtName.setText(String.valueOf(productList.get(position).getName()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductDialogFragment dialogFragment = new ProductDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("name",productList.get(position).getName());
                    bundle.putString("desc",productList.get(position).getDescription());
                    bundle.putDouble("price",productList.get(position).getPrice());
                    bundle.putString("image",productList.get(position).getImage());
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(fragmentActivity.getSupportFragmentManager(),"dialog_product");

                    if(onItemClickListener != null) {
                        onItemClickListener.onItemClick(productList.get(position));
                    }
                }
            });
        }

    }
}
