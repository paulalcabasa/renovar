package com.drei.renovarapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.drei.renovarapp.Adapters.TherapySelectionAdapter;
import com.drei.renovarapp.Models.Product;
import com.drei.renovarapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TherapySelection extends AppCompatActivity implements TherapySelectionAdapter.TherapySelectionListener {

    private static final String TAG = "TherapySelection";
    private RecyclerView recyclerViewTherapySelection;
    private List<Product> productList;
    private TherapySelectionAdapter therapySelectionAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapy_selection);

        recyclerViewTherapySelection = findViewById(R.id.recyclerViewTherapySelection);
        productList = new ArrayList<>();

        loadCatalog();
    }


    public void loadCatalog() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://renovar.health/renovarmobile/get_products.php?category=1",
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


                            therapySelectionAdapter = new TherapySelectionAdapter(TherapySelection.this,productList,TherapySelection.this);
                            recyclerViewTherapySelection.setLayoutManager(new LinearLayoutManager(TherapySelection.this));
                            recyclerViewTherapySelection.setAdapter(therapySelectionAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + error.getMessage());
                        Toast.makeText(TherapySelection.this, "Unable to connect, please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onTherapySelect(Product product) {
        Intent intent = new Intent();
        intent.putExtra("product",product);
        setResult(RESULT_OK,intent);
        finish();
    }
}
