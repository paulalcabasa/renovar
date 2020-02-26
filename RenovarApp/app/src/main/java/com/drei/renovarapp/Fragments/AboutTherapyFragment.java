package com.drei.renovarapp.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Transformers.DefaultTransformer;
import com.daimajia.slider.library.Transformers.ZoomOutTransformer;
import com.drei.renovarapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

public class AboutTherapyFragment extends Fragment {


    SliderLayout sliderAbout;

    public AboutTherapyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_therapy, container, false);
        sliderAbout = view.findViewById(R.id.sliderAbout);

        sliderAbout.setDuration(3000);
        sliderAbout.setPagerTransformer(true,new DefaultTransformer());
        initAboutSlider();

        return view;
    }


    public void initAboutSlider()
    {
        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.slide1);
        imageList.add(R.drawable.slide2);
        imageList.add(R.drawable.slide3);

        for(Integer integer : imageList)
        {
            DefaultSliderView defaultSliderView = new DefaultSliderView(getContext());
            defaultSliderView.setPicasso(Picasso.get());
            defaultSliderView.image(integer);
            defaultSliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
            sliderAbout.addSlider(defaultSliderView);
        }

    }

}
