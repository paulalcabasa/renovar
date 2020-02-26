package com.drei.renovarapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.drei.renovarapp.Database.CartDB;
import com.drei.renovarapp.MainActivity;
import com.drei.renovarapp.Models.Checkout;
import com.drei.renovarapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class AddToCartActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextInputEditText txtCountry,txtEmailAddress,txtFirstname,txtLastname,txtStreet1,txtStreet2,txtProvince,txtZip,txtCity;
    TextView txtGrandTotal;
    MaterialButton btnProceed;
    String[] mTestArray;
    List<CartDB> cartDBList;
    double total = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        setUpToolbar(getWindow().getDecorView().getRootView());

        txtCountry= findViewById(R.id.txtCountry);
        txtEmailAddress= findViewById(R.id.txtEmailAddress);
        txtFirstname= findViewById(R.id.txtFirstname);
        txtLastname= findViewById(R.id.txtLastname);
        txtStreet1= findViewById(R.id.txtStreet1);
        txtStreet2= findViewById(R.id.txtStreet2);
        txtProvince= findViewById(R.id.txtProvince);
        txtZip= findViewById(R.id.txtZipCode);
        txtCity = findViewById(R.id.txtCity);
        btnProceed = findViewById(R.id.btnProceed);
        txtGrandTotal = findViewById(R.id.txtGrandTotal);

        CartDB cartDB = CartDB.getInstance(AddToCartActivity.this);
        cartDB.open();
        cartDBList = cartDB.getCart();
        cartDB.close();


        for(CartDB cart : cartDBList)
        {
            total+= cart.getTotal_price();
        }
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance(Locale.US));
        txtGrandTotal.setText(format.format(total));

        mTestArray = getResources().getStringArray(R.array.countries_arrays);
        txtCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(AddToCartActivity.this);
                dialog.setContentView(R.layout.country_dialog);
                ListView lv = (ListView ) dialog.findViewById(R.id.List);
                ArrayAdapter<String> itemsAdapter =
                        new ArrayAdapter<String>(AddToCartActivity.this, android.R.layout.simple_dropdown_item_1line,mTestArray);
                lv.setAdapter(itemsAdapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        txtCountry.setText(mTestArray[i]);
                        dialog.dismiss();
                    }
                });
                dialog.setCancelable(true);
                dialog.setTitle("Select Country");
                dialog.show();

            }
        });


        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstname = txtFirstname.getText().toString();
                String lastname = txtLastname.getText().toString();
                String email = txtEmailAddress.getText().toString();
                String street1 = txtStreet1.getText().toString();
                String street2 = txtStreet2.getText().toString();
                String country = txtCountry.getText().toString();
                String province = txtProvince.getText().toString();
                String zip = txtZip.getText().toString();
                String city = txtCity.getText().toString();




                String json = new Gson().toJson(cartDBList);

                if(TextUtils.isEmpty(txtFirstname.getText()) && TextUtils.isEmpty(txtLastname.getText()) && TextUtils.isEmpty(txtEmailAddress.getText())
                    && TextUtils.isEmpty(txtStreet2.getText()) && TextUtils.isEmpty(txtStreet1.getText()) && TextUtils.isEmpty(txtCountry.getText())
                    && TextUtils.isEmpty(txtProvince.getText()) && TextUtils.isEmpty(txtZip.getText()) && TextUtils.isEmpty(txtCity.getText())) {
                    Toast.makeText(AddToCartActivity.this, "Please fill up all fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Checkout checkout = new Checkout(email, firstname, lastname, street1, street2, country, city, province, zip, json, new Checkout.AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            Toast.makeText(AddToCartActivity.this, output, Toast.LENGTH_SHORT).show();
                            if (!output.contains("failed")) {
                                CartDB cartDB = CartDB.getInstance(AddToCartActivity.this);
                                cartDB.open();
                                cartDB.deleteAllRecord();
                                cartDB.close();
                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                        }
                    });
                    checkout.execute();
                }
            }
        });
    }

    private void setUpToolbar(View view) {
        toolbar = findViewById(R.id.app_bar);
        toolbar.setNavigationContentDescription("nav");
        AppCompatActivity activity = (AppCompatActivity) AddToCartActivity.this;
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
}


