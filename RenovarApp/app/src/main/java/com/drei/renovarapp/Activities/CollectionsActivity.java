package com.drei.renovarapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.drei.renovarapp.Adapters.CatalogAdapter;
import com.drei.renovarapp.Adapters.CategoryAdapter;
import com.drei.renovarapp.Models.Category;
import com.drei.renovarapp.Models.Collection;
import com.drei.renovarapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CollectionsActivity extends AppCompatActivity implements CategoryAdapter.OnItemClickListener{

    private static final int REQUEST_CATEGORY = 3;
    private RecyclerView recyclerViewCategory;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private CatalogAdapter catalogAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        recyclerViewCategory = findViewById(R.id.recyclerViewProductsCategory);
        categoryList = new ArrayList<>();

        loadCategories();

        setUpToolbar(toolbar);

        setTitle("SELECT THERAPY");
    }


    private void setUpToolbar(View view) {
        toolbar = findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) CollectionsActivity.this;
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

    public void loadCategories() {
        AppCompatActivity appCompatActivity = CollectionsActivity.this;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://renovar.health/renovarmobile/get_categories.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                categoryList.add(new Category(object.getInt("id"),
                                        object.getString("category"),
                                        object.getString("image_url")));
                            }

                            categoryAdapter = new CategoryAdapter(getApplicationContext(),categoryList,true,CollectionsActivity.this);
                            recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerViewCategory.setAdapter(categoryAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext().getApplicationContext(), "Unable to connect, please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CATEGORY && resultCode == RESULT_OK && data != null)
        {
            Intent intent = new Intent();
            intent.putExtra("product", data.getSerializableExtra("product"));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onItemClick(Category item) {
        Intent intent = new Intent(this, ProductsActivity.class);
        intent.putExtra("id",item.getId());
        intent.putExtra("isSelection",true);
        startActivityForResult(intent,REQUEST_CATEGORY);
    }
}
