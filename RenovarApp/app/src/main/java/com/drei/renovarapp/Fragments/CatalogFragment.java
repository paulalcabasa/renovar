package com.drei.renovarapp.Fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class CatalogFragment extends Fragment {

    private static final String TAG = "CatalogFragment";
    private RecyclerView recyclerViewCatalog;
    private List<Product> productList;
    private CatalogAdapter catalogAdapter;
    private LottieAnimationView animation;

    public CatalogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        recyclerViewCatalog = view.findViewById(R.id.recyclerViewCatalog);
        animation = view.findViewById(R.id.animation);
        productList = new ArrayList<>();


        loadCatalog();


        return view;
    }

    public void loadCatalog() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://renovar.health/renovarmobile/get_products.php?category=" + getArguments().getString("category"),
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
                                        object.getString("description")));
                                Log.i(TAG, "onResponse: " + object.getString("name"));
                            }

                            catalogAdapter = new CatalogAdapter(getActivity(), getActivity().getApplicationContext(), productList);
                            recyclerViewCatalog.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                            recyclerViewCatalog.setAdapter(catalogAdapter);
                            animation.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + error.getMessage());
                        animation.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext().getApplicationContext(), "Unable to connect, please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

}

