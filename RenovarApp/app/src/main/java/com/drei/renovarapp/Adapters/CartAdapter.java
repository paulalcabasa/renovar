package com.drei.renovarapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drei.renovarapp.Database.CartDB;
import com.drei.renovarapp.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    public List<CartDB> cartDBList;

    public CartAdapter(Context context, List<CartDB> cartDBList) {
        this.context = context;
        this.cartDBList = cartDBList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CartViewHolder cartViewHolder = (CartViewHolder)holder;
        cartViewHolder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return cartDBList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        ImageView imageProduct;
        TextView txtAmountQuanity,txtProductName;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProduct = itemView.findViewById(R.id.imageProduct);
            txtAmountQuanity = itemView.findViewById(R.id.txtAmountQuantity);
            txtProductName = itemView.findViewById(R.id.txtProductName) ;
        }

        public void bindData(int position)
        {
            txtProductName.setText(cartDBList.get(position).getItem_name());
            Picasso.get().load(cartDBList.get(position).getImage_url()).into(imageProduct);
            NumberFormat format = NumberFormat.getCurrencyInstance();
            format.setCurrency(Currency.getInstance(Locale.US));
            txtAmountQuanity.setText(format.format(cartDBList.get(position).getTotal_price()) +" (" +cartDBList.get(position).getQuantity() +" pc/s)");
        }
    }
}
