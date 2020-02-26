package com.drei.renovarapp.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drei.renovarapp.Adapters.TherapyAdapter;
import com.drei.renovarapp.Database.TherapyDB;
import com.drei.renovarapp.R;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TherapyFragment extends androidx.fragment.app.Fragment {
    private RecyclerView recyclerViewTherapies;
    private TherapyAdapter therapyAdapter;
    private List<TherapyDB> therapyList;
    private TextView lblNoSched;

//    private ImageView imageProgress;


    public TherapyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_therapy, container, false);

        lblNoSched = view.findViewById(R.id.lblNoSched);

        recyclerViewTherapies = view.findViewById(R.id.recyclerViewTherapies);
        setItems();
        therapyAdapter = new TherapyAdapter(getActivity().getApplicationContext(), therapyList, getActivity());
        recyclerViewTherapies.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTherapies.setAdapter(therapyAdapter);

        return view;
    }


    public void setItems() {
        TherapyDB therapyDB = TherapyDB.getInstance(getContext());
        therapyDB.open();
        therapyList = therapyDB.getTherapyList();
        therapyDB.close();

        if (therapyList.size() <= 0) {
            lblNoSched.setVisibility(View.VISIBLE);
        }
        else
        {
            lblNoSched.setVisibility(View.GONE);
        }
    }

}
