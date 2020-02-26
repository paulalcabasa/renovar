package com.drei.renovarapp.FaceChange;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.drei.renovarapp.Database.ResultDB;
import com.drei.renovarapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends androidx.fragment.app.Fragment {


    private ImageView imageProgress;
    private TextView txtStatus;

    public ProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        imageProgress = view.findViewById(R.id.imageProgress);
        txtStatus = view.findViewById(R.id.txtStatus);
        final ResultDB resultDB = (ResultDB) getArguments().getSerializable("result");

        File file = new File(resultDB.getPath_after());
        Picasso.get().load(file).into(imageProgress);

        imageProgress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (txtStatus.getText().toString().equals("AFTER")) {
                    txtStatus.setText("BEFORE");
                    File fileAfter = new File(resultDB.getPath_before());
                    Picasso.get().load(fileAfter).into(imageProgress);
                }
                else{
                    txtStatus.setText("AFTER");
                    File file = new File(resultDB.getPath_after());
                    Picasso.get().load(file).into(imageProgress);
                }
                return false;
            }
        });

        return view;
    }

}
