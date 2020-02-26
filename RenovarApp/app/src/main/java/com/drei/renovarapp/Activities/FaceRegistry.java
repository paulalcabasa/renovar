package com.drei.renovarapp.Activities;

import android.os.Bundle;

import com.drei.renovarapp.FaceChange.BeforeFragment;
import com.github.paolorotolo.appintro.AppIntro;

public class FaceRegistry extends AppIntro {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_face_registry);

        addSlide(new BeforeFragment());
    }
}
