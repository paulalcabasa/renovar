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
import com.drei.renovarapp.Database.TherapyDB;
import com.drei.renovarapp.R;
import com.drei.renovarapp.Receivers.AlarmReceiver;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class TherapyDialogFragment extends BottomSheetDialogFragment {

    private MaterialButton btnDelete,btnResult;

    public TherapyDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_therapy_dialog, container, false);

        btnDelete = view.findViewById(R.id.btnDelete);
        btnResult = view.findViewById(R.id.btnResult);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                int id = bundle.getInt("id");

                TherapyDB therapyDB = TherapyDB.getInstance(getContext());
                therapyDB.open();
                therapyDB.setId(id);
                if(therapyDB.deleteRecord())
                {
//                    Intent intent = new Intent(getActivity(), AlarmService.class);
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
//                    alarmManager.cancel(pendingIntent);
//                    pendingIntent.cancel();

                    Intent intent = new Intent(getContext().getApplicationContext(), AlarmReceiver.class);
                    AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(pendingIntent);
                    pendingIntent.cancel();
                    Toast.makeText(getActivity(), "Schedule Canceled", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                therapyDB.close();

            }
        });

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), CameraActivity.class);
                Bundle bundle = getArguments();
                int id = bundle.getInt("id");
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        return view;
    }

}
