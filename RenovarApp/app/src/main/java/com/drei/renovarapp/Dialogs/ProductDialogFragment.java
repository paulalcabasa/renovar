package com.drei.renovarapp.Dialogs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.drei.renovarapp.Database.CartDB;
import com.drei.renovarapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDialogFragment extends BottomSheetDialogFragment {

    TextView txtName,txtDescription,txtPrice,txtAmountQuantity;
    SliderLayout sliderProduct;
    PagerIndicator pagerIndicator;
    MaterialButton btnAdd,btnRemove,btnAddToCart;
    int quantity = 1;

    public ProductDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_dialog, container, false);
        txtName = view.findViewById(R.id.txtName);
        txtDescription = view.findViewById(R.id.txtDescription);
        txtPrice = view.findViewById(R.id.txtPrice);
        sliderProduct = view.findViewById(R.id.sliderProduct);
        pagerIndicator = view.findViewById(R.id.pagerIndicator);
        txtAmountQuantity = view.findViewById(R.id.txtAmountQuantity);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnRemove = view.findViewById(R.id.btnRemove);
        btnAddToCart = view.findViewById(R.id.btnAddToCart);

        compute();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity++;
                compute();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(quantity <= 1)) {
                    quantity--;
                    compute();
                }
            }
        });


        DefaultSliderView defaultSliderView = new DefaultSliderView(getContext());
        Bundle bundle = getArguments();

        txtName.setText(bundle.getString("name"));
        txtDescription.setText(bundle.getString("desc"));
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance(Locale.US));
        txtPrice.setText(format.format(bundle.getDouble("price",0)));
        defaultSliderView.setPicasso(Picasso.get());
        defaultSliderView.setScaleType(BaseSliderView.ScaleType.FitCenterCrop);
        defaultSliderView.image(bundle.getString("image"));

        sliderProduct.setCustomIndicator(pagerIndicator);
        sliderProduct.addSlider(defaultSliderView);


        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getArguments();
                double total_price = bundle.getDouble("price",0) * quantity;
                String product_name =  bundle.getString("name");
                String image_url = bundle.getString("image");

                CartDB cartDB = CartDB.getInstance(getActivity());
                cartDB.setTotal_price(total_price);
                cartDB.setItem_name(product_name);
                cartDB.setImage_url(image_url);
                cartDB.setQuantity(quantity);


                boolean isInsert = cartDB.insertRecord();


                if(isInsert) {
                    Toast.makeText(getActivity(), "Product Added", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                else {
                    Toast.makeText(getActivity(), "Failed adding to cart", Toast.LENGTH_SHORT).show();
                    dismiss();
                }

            }
        });
        return view;
    }

    public void compute()
    {
        Bundle bundle = getArguments();
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance(Locale.US));
        txtAmountQuantity.setText(format.format(bundle.getDouble("price",0) * quantity) + " ("+quantity+" pc/s)");
    }

}
