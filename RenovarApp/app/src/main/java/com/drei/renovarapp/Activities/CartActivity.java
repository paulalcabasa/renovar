package com.drei.renovarapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.drei.renovarapp.Adapters.CartAdapter;
import com.drei.renovarapp.Database.CartDB;
import com.drei.renovarapp.MainActivity;
import com.drei.renovarapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private static final int REQUEST_PROCEED = 23;
    Toolbar toolbar;
    RecyclerView recyclerViewCart;
    List<CartDB> cartDBList;
    CartAdapter cartAdapter;
    private static final String TAG = "CartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        setUpToolbar(getWindow().getDecorView().getRootView());
        this.setTitle("Shopping Cart");
        cartDBList = new ArrayList<>();

        CartDB cartDB = CartDB.getInstance(CartActivity.this);
        cartDB.open();
        cartDBList = cartDB.getCart();
        cartDB.close();

        String json = new Gson().toJson(cartDBList);



        cartAdapter = new CartAdapter(this,cartDBList);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();


        Log.i(TAG, "JSON : " + json);
    }

    private void setUpToolbar(View view) {
        toolbar = findViewById(R.id.app_bar);
        toolbar.setNavigationContentDescription("nav");
        AppCompatActivity activity = (AppCompatActivity) CartActivity.this;
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.proceed:
                if(!(cartDBList.size() <= 0)) {
                    Intent intent = new Intent(CartActivity.this, AddToCartActivity.class);
                    startActivityForResult(intent, REQUEST_PROCEED);
                }else {
                    Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_PROCEED && resultCode == Activity.RESULT_OK)
        {
            finish();
        }
    }
}
