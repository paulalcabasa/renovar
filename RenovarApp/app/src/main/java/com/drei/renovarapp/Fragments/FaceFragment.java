package com.drei.renovarapp.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drei.renovarapp.Adapters.ResultAdapter;
import com.drei.renovarapp.Adapters.ViewPagerAdapter;
import com.drei.renovarapp.Database.ResultDB;
import com.drei.renovarapp.FaceChange.PlayProgressActivity;
import com.drei.renovarapp.FaceChange.ProgressFragment;
import com.drei.renovarapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class FaceFragment extends Fragment {

//    private MaterialCalendarView calendarView;
//    private ViewPager pagerTherapies;
    private ViewPagerAdapter adapter;
    private List<Calendar> calendarList;
//    private TextView txtDateNow;
    private List<ResultDB> resultList;
    private RecyclerView recyclerViewResults;
    private ResultAdapter resultAdapter;
    private TextView txtNoRecord;
    private FloatingActionButton fabPlay;

    public FaceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_face, container, false);
        recyclerViewResults = view.findViewById(R.id.recyclerViewResults);
        txtNoRecord = view.findViewById(R.id.txtNoRecord);
        fabPlay = view.findViewById(R.id.fabPlay);
//        calendarView = view.findViewById(R.id.calendarView);
//        pagerTherapies = view.findViewById(R.id.pagerTherapies);
//        txtDateNow = view.findViewById(R.id.txtDateNow);
        resultList = new ArrayList<>();

        setData();

        resultAdapter = new ResultAdapter(getActivity(),getContext(),resultList);
        recyclerViewResults.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerViewResults.setAdapter(resultAdapter);


//        setDateText(calendarNow);
//        calendarView.setSelectedDate(calendarNow);
//        calendarView.setDynamicHeightEnabled(true);

        fabPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PlayProgressActivity.class));
            }
        });

        return view;
    }
//
//    public void setDateText(Calendar calendarNow) {
//        final DateFormat format = new DateFormat();
//        Calendar compare = Calendar.getInstance();
//        if (!(calendarNow == compare)) {
//            txtDateNow.setText(format.format("dd, E MMM", calendarNow));
//        } else {
//            txtDateNow.setText("Today" + format.format(", E MMM", calendarNow));
//        }
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
//        for (int i = 0; i < 10; i++) {
//            //passvalue to getitems
//            adapter.addFragment(new TherapyFragment(), String.valueOf(i));
//        }

        for (ResultDB result : resultList) {
            ProgressFragment progressFragment = new ProgressFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("result", result);
            progressFragment.setArguments(bundle);
            adapter.addFragment(progressFragment, result.getPath_before());
        }
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
    }

    public void setData() {
        ResultDB resultDB = ResultDB.getInstance(getContext());
        resultDB.open();
        resultList = resultDB.getResultListGroup();
        resultDB.close();

        if(resultList.size() <= 0)
        {
            txtNoRecord.setVisibility(View.VISIBLE);
        }
        else
        {
            txtNoRecord.setVisibility(View.GONE);
        }
    }

}
