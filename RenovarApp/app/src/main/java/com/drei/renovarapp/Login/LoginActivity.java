package com.drei.renovarapp.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Transformers.ZoomOutTransformer;
import com.drei.renovarapp.MainActivity;
import com.drei.renovarapp.Models.Authentication;
import com.drei.renovarapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private SliderLayout sliderLogin;
    private ImageView logo;
//    Animation.AnimationListener listener;
    private TextInputEditText txtEmailAddress, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        txtEmailAddress = findViewById(R.id.txtEmailAddress);
        txtPassword = findViewById(R.id.txtPassword);
        sliderLogin = findViewById(R.id.sliderLogin);
        logo = findViewById(R.id.logo);

//        listener = new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                //load_animations();
//            }
//        };
//
//        loadAnimations();


        boolean hasSession;
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        hasSession = sharedPref.getBoolean("logon", false);

        if (hasSession)
        {
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }

        sliderLogin.setPagerTransformer(true, new ZoomOutTransformer());
        initAboutSlider();
    }

    public void registerSession()
    {
        String username = txtEmailAddress.getText().toString();
        String password = txtPassword.getText().toString();
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("logon", true);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

//    void loadAnimations() {
//        new AnimationUtils();
//        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
//        rotation.setAnimationListener(listener);
//        logo.startAnimation(rotation);
//    }

    public void signup(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    public void login(View view) {
        Authentication authentication = new Authentication(txtEmailAddress.getText().toString(), txtPassword.getText().toString(), new Authentication.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                if(output.contains("success")) {
                    finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    registerSession();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Login failed, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        authentication.execute();
    }

    public void initAboutSlider() {
        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.slide1);
        imageList.add(R.drawable.slide2);
        imageList.add(R.drawable.slide3);

        for (Integer integer : imageList) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(this);
            defaultSliderView.setPicasso(Picasso.get());
            defaultSliderView.image(integer);
            defaultSliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
            sliderLogin.addSlider(defaultSliderView);
        }

    }


    public void startBackgroundVideo() {
//        String vidAddress = "https://vimeo.com/71565171";
//        Uri vidUri = Uri.parse(vidAddress);
//        backgroundVideo.setVideoURI(vidUri);
//        backgroundVideo.start();
    }
}
