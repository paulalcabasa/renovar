package com.drei.renovarapp.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drei.renovarapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScienceFragment extends androidx.fragment.app.Fragment {


    public ScienceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_science, container, false);
    }

}
