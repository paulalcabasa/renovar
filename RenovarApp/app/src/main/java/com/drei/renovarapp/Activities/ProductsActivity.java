package com.drei.renovarapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.drei.renovarapp.Adapters.CatalogAdapter;
import com.drei.renovarapp.Models.Product;
import com.drei.renovarapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity implements CatalogAdapter.OnItemClickListener {

    private static final String TAG = "CatalogFragment";
    private RecyclerView recyclerViewCatalog;
    private List<Product> productList;
    private CatalogAdapter catalogAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        recyclerViewCatalog = findViewById(R.id.recyclerViewProducts);
        productList = new ArrayList<>();

        setUpToolbar(toolbar);

        this.setTitle("Collections");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadCatalog();
    }


    private void setUpToolbar(View view) {
        toolbar = findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) ProductsActivity.this;
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

    public void loadCatalog() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://renovar.health/renovarmobile/get_product_collection.php?collection_id=" + getIntent().getIntExtra("id",0),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                productList.add(new Product(object.getInt("id"),
                                        object.getString("image_url"),
                                        object.getString("name"),
                                        object.getString("description"),
                                        object.getDouble("price"),
                                        object.getInt("interval"),
                                        object.getString("cam_message")));
                                Log.i(TAG, "onResponse: " + object.getString("name"));
                            }

                            catalogAdapter = new CatalogAdapter(ProductsActivity.this, getApplicationContext(), productList, ProductsActivity.this);
                            recyclerViewCatalog.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                            recyclerViewCatalog.setAdapter(catalogAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), "Unable to connect, please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    @Override
    public void onItemClick(Product item) {
        if(getIntent().getBooleanExtra("isSelection",false)) {
            Intent intent = new Intent();
            intent.putExtra("product", item);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}

