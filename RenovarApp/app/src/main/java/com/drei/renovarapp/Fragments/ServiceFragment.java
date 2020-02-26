package com.drei.renovarapp.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drei.renovarapp.Adapters.ViewPagerAdapter;
import com.drei.renovarapp.R;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends Fragment {
    ViewPager viewpagerTabs;
    TabLayout tabLayout;
    ViewPagerAdapter adapter;

    public ServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service, container, false);
        viewpagerTabs = view.findViewById(R.id.viewpagerTabs);
        tabLayout = view.findViewById(R.id.tabLayout);

        tabLayout.setupWithViewPager(viewpagerTabs,true);
        setupViewPager(viewpagerTabs);


        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new LiftetimeWarrantyFragment(), "Lifetime Warranty");
        adapter.addFragment(new WarrantyClaimFragment(), "Warranty Claim");
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
    }

}
