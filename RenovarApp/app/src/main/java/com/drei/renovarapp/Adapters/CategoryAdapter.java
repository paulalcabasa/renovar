package com.drei.renovarapp.Adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.drei.renovarapp.Activities.ViewCollectionActivity;
import com.drei.renovarapp.Models.Category;
import com.drei.renovarapp.Models.Collection;
import com.drei.renovarapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int REQUEST_CODE_CATEGORY = 2;
    private Context context;
    private List<Category> categoryList;
    private boolean isSelection;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Category item);
    }


    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    public CategoryAdapter(Context context, List<Category> categoryList, boolean isSelection) {
        this.context = context;
        this.categoryList = categoryList;
        this.isSelection = isSelection;
    }

    public CategoryAdapter(Context context, List<Category> categoryList, boolean isSelection, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.categoryList = categoryList;
        this.isSelection = isSelection;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category,parent,false);
        return new ViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderCategory viewHolderCategory = (ViewHolderCategory)holder;
        viewHolderCategory.bindData(position);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolderCategory extends RecyclerView.ViewHolder{
        private ImageView imageCategory;
        private TextView txtCategoryName;
        private View view;

        public ViewHolderCategory(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            imageCategory = itemView.findViewById(R.id.imageCategory);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
        }

        public void bindData(final int position)
        {
            Picasso.get().load(categoryList.get(position).getImage_url()).fit().centerCrop().into(imageCategory);
            txtCategoryName.setText(categoryList.get(position).getCategory());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isSelection)
                    {
                        onItemClickListener.onItemClick(categoryList.get(position));
                    }
                    else {
                        Intent intent = new Intent(context, ViewCollectionActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("image_url", categoryList.get(position).getImage_url());
                        intent.putExtra("name", categoryList.get(position).getCategory());
                        intent.putExtra("id", categoryList.get(position).getId());
                        intent.putExtra("isSelection", isSelection);
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
