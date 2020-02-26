package com.drei.renovarapp.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.drei.renovarapp.Adapters.CatalogAdapter;
import com.drei.renovarapp.Adapters.CategoryAdapter;
import com.drei.renovarapp.Models.Category;
import com.drei.renovarapp.Models.Product;
import com.drei.renovarapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends androidx.fragment.app.Fragment {

    private RecyclerView recyclerViewCategory;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    public ProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        recyclerViewCategory = view.findViewById(R.id.recyclerViewProductsCategory);
        categoryList = new ArrayList<>();

        loadCategories();

        return view;
    }

    public void loadCategories() {
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

                            categoryAdapter = new CategoryAdapter(getContext(),categoryList);
                            recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerViewCategory.setAdapter(categoryAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext().getApplicationContext(), "Unable to connect, please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }


}
