package com.drei.renovarapp.Adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.drei.renovarapp.Activities.ProductsActivity;
import com.drei.renovarapp.Activities.ViewCollectionActivity;
import com.drei.renovarapp.Models.Collection;
import com.drei.renovarapp.Models.Product;
import com.drei.renovarapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class CollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int REQUEST_COLLECTION = 1;
    private Context context;
    private List<Collection> collectionList;
    private boolean isSelection;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Collection item);
    }

    public CollectionAdapter(Context context, List<Collection> collectionList) {
        this.context = context;
        this.collectionList = collectionList;
    }

    public CollectionAdapter(Context context, List<Collection> collectionList, boolean isSelection) {
        this.context = context;
        this.collectionList = collectionList;
        this.isSelection = isSelection;
    }

    public CollectionAdapter(Context context, List<Collection> collectionList, boolean isSelection, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.collectionList = collectionList;
        this.isSelection = isSelection;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_collection,parent,false);
        return new ViewHolderCollection(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderCollection viewHolderCollection = (ViewHolderCollection)holder;
        viewHolderCollection.bindData(position);
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    public class ViewHolderCollection extends RecyclerView.ViewHolder
    {
        private ImageView imageCategory;
        private TextView txtCategoryName;
        private View view;
        public ViewHolderCollection(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            imageCategory = itemView.findViewById(R.id.imageCategory);
            txtCategoryName = itemView.findViewById(R.id.txtCollectionName);
        }

        public void bindData(final int position)
        {
            Picasso.get().load(collectionList.get(position).getImage_url()).fit().centerCrop().into(imageCategory);
            txtCategoryName.setText(collectionList.get(position).getCollection());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isSelection) {
                        onItemClickListener.onItemClick(collectionList.get(position));
                    }
                    else {
                        Intent intent = new Intent(context, ProductsActivity.class);
                        intent.putExtra("id",collectionList.get(position).getId());
                        intent.putExtra("isSelection",isSelection);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Bundle b = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
                            b = ActivityOptions.makeThumbnailScaleUpAnimation(v, bitmap, 0, 0).toBundle();
                        }
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
