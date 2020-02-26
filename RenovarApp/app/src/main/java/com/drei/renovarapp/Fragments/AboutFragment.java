package com.drei.renovarapp.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drei.renovarapp.Adapters.ViewPagerAdapter;
import com.drei.renovarapp.R;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;

public class AboutFragment extends androidx.fragment.app.Fragment {

    ViewPager viewpagerTabs;
    TabLayout tabLayout;
    ViewPagerAdapter adapter;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        viewpagerTabs = view.findViewById(R.id.viewpagerTabs);
        tabLayout = view.findViewById(R.id.tabLayout);

        tabLayout.setupWithViewPager(viewpagerTabs,true);
        setupViewPager(viewpagerTabs);
        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new AboutTherapyFragment(), "Renovar");
        adapter.addFragment(new ScienceFragment(), "Technology");
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
    }


}
