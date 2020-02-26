package com.drei.renovarapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.drei.renovarapp.Adapters.CategoryAdapter;
import com.drei.renovarapp.Adapters.CollectionAdapter;
import com.drei.renovarapp.Models.Category;
import com.drei.renovarapp.Models.Collection;
import com.drei.renovarapp.Models.Product;
import com.drei.renovarapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewCollectionActivity extends AppCompatActivity implements CollectionAdapter.OnItemClickListener {

    private static final int REQUEST_COLLECTIONS = 1;
    private ImageView imageCollection;
    private TextView txtCollectionName;
    private RecyclerView recyclerViewCollection;
    private CollectionAdapter collectionAdapter;
    private List<Collection> collectionList;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_collection);
        collectionList = new ArrayList<>();
        recyclerViewCollection = findViewById(R.id.recyclerViewCollectionItems);

        imageCollection = findViewById(R.id.imageCollection);
        txtCollectionName = findViewById(R.id.txtCollectionName);



        Picasso.get().load(getIntent().getStringExtra("image_url")).fit().centerCrop().into(imageCollection);
        txtCollectionName.setText(getIntent().getStringExtra("name"));

        setUpToolbar(toolbar);

        this.setTitle(getIntent().getStringExtra("name"));

        loadCollections();
    }


    private void setUpToolbar(View view) {
        toolbar = findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) ViewCollectionActivity.this;
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home :
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadCollections() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://renovar.health/renovarmobile/get_collections.php?category_id="+getIntent().getIntExtra("id",0),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                collectionList.add(new Collection(object.getInt("id"),
                                        object.getString("collection"),
                                        object.getString("image_url"),
                                        object.getInt("category_id")));
                            }

                            collectionAdapter = new CollectionAdapter(getApplicationContext(),collectionList,getIntent().getBooleanExtra("isSelection",false),ViewCollectionActivity.this);
                            recyclerViewCollection.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerViewCollection.setAdapter(collectionAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Unable to connect, please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_COLLECTIONS && resultCode == RESULT_OK && data != null) {
            Intent intent = new Intent();
            intent.putExtra("product", data.getSerializableExtra("product"));
            setResult(RESULT_OK, intent);
            finish();
        }
    }


    @Override
    public void onItemClick(Collection item) {
        Intent intent = new Intent(ViewCollectionActivity.this, ProductsActivity.class);
        intent.putExtra("id",item.getId());
        intent.putExtra("isSelection",true);
        startActivityForResult(intent,REQUEST_COLLECTIONS);
    }
}
