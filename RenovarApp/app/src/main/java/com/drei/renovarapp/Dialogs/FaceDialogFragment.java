package com.drei.renovarapp.Dialogs;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.drei.renovarapp.Activities.CameraActivity;
import com.drei.renovarapp.Database.ResultDB;
import com.drei.renovarapp.Database.TherapyDB;
import com.drei.renovarapp.R;
import com.drei.renovarapp.Receivers.AlarmReceiver;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.io.File;

import androidx.fragment.app.DialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FaceDialogFragment extends BottomSheetDialogFragment {
    private MaterialButton btnDelete;

    public FaceDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_face_dialog, container, false);

        btnDelete = view.findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                long date = bundle.getLong("id");
                String beforefile = bundle.getString("before");
                String afterfile = bundle.getString("after");
                ResultDB resultDB = ResultDB.getInstance(getContext());
                resultDB.open();
                if(resultDB.deleteRecord(date))
                {
                    Toast.makeText(getActivity(), "Record Deleted", Toast.LENGTH_SHORT).show();
                    File file = new File(beforefile);
                    file.delete();

                    File file2 = new File(afterfile);
                    file2.delete();
                    dismiss();
                }
                resultDB.close();

            }
        });


        return view;
    }

}
