package com.drei.renovarapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Transformers.DefaultTransformer;
import com.daimajia.slider.library.Transformers.ZoomInTransformer;
import com.daimajia.slider.library.Transformers.ZoomOutTransformer;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.drei.renovarapp.Database.ResultDB;
import com.drei.renovarapp.R;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.daimajia.androidanimations.library.Techniques.FadeIn;
import static com.daimajia.androidanimations.library.Techniques.SlideInLeft;

public class HomeActivity extends AppCompatActivity {

    SliderLayout sliderAbout;
    TextView txtMessage;
    MaterialButton btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_home);
        sliderAbout = findViewById(R.id.sliderAbout);
        txtMessage = findViewById(R.id.txtMessage);
        btnClose = findViewById(R.id.btnClose);


        sliderAbout.setDuration(6000);
        sliderAbout.setPagerTransformer(true,new ZoomOutTransformer());
        initAboutSlider();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    List<String> text = new ArrayList<>();
    public void initAboutSlider()
    {
        List<String> imageList = new ArrayList<>();
        imageList.add("http://renovar.health/renovarmobile/slide1.jpg");
        imageList.add("http://renovar.health/renovarmobile/slide2.jpg");
        imageList.add("http://renovar.health/renovarmobile/slide3.jpg");

        text.add("<h1>YOUR WISH IS<br>OUR TECHNOLOGY</h1>");
        text.add("<h1>WE BELIEVE</h1>\nEveryone is entitled to\ndiscover a new world of advanced technology");
        text.add("<h1>OUR PHILOSOPHY</h1>\nTo help relieve\npain and issues related to\n Health and Wellness");

        for(String urls : imageList)
        {
            DefaultSliderView defaultSliderView = new DefaultSliderView(getApplicationContext());
            defaultSliderView.setPicasso(Picasso.get());
            defaultSliderView.image(urls);
            defaultSliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
            sliderAbout.addSlider(defaultSliderView);
        }


        sliderAbout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                txtMessage.setText(Html.fromHtml(text.get(position)));
                YoYo.with(SlideInLeft).duration(700).playOn(txtMessage);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
