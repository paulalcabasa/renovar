package com.drei.renovarapp.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.drei.renovarapp.Database.TherapyDB;
import com.drei.renovarapp.Models.Product;
import com.drei.renovarapp.R;
import com.drei.renovarapp.Receivers.AlarmReceiver;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ScheduleActivity extends AppCompatActivity {

    private static final int REQUEST_THERAPY_PRODUCT = 1;
    Toolbar toolbar;
    EditText txtTime, txtTherapy;
    MaterialButton btnDone;

    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    TimePickerDialog mTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        txtTime = findViewById(R.id.txtTime);
        btnDone = findViewById(R.id.btnDone);
        txtTherapy = findViewById(R.id.txtTherapy);

        Calendar calendar = Calendar.getInstance();
        setCalendarText(calendar);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTimePicker();
            }
        });

        txtTherapy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleActivity.this, CollectionsActivity.class);
                intent.putExtra("isSelection",true);
                startActivityForResult(intent, REQUEST_THERAPY_PRODUCT);
            }
        });

        setUpToolbar(getWindow().getDecorView().getRootView());
        setTitle("New Therapy Schedule");
    }

    private void setUpToolbar(View view) {
        toolbar = findViewById(R.id.app_bar);
        toolbar.setNavigationContentDescription("nav");
        AppCompatActivity activity = (AppCompatActivity) ScheduleActivity.this;
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    Product product = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_THERAPY_PRODUCT && resultCode == RESULT_OK && data != null) {
            product = (Product) data.getSerializableExtra("product");
            txtTherapy.setText(product.getName());
        }
    }

    public void initTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(ScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, final int selectedHour, final int selectedMinute) {
                final Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendar.set(Calendar.MINUTE, selectedMinute);
                setCalendarText(calendar);
                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSchedule(calendar);
                    }
                });
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void setCalendarText(Calendar calendar) {
        String am_pm = "";
        if (calendar.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (calendar.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        String strHrsToShow = (calendar.get(Calendar.HOUR) == 0) ? "12" : calendar.get(Calendar.HOUR) + "";
        txtTime.setText(strHrsToShow + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + " " + am_pm);
    }

    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    public void setSchedule(Calendar calendarRecord) {
        TherapyDB therapyDB = TherapyDB.getInstance(this);
        therapyDB.open();
        int id = therapyDB.generateID();
        therapyDB.setId(id);
        therapyDB.setDuration(product.getInterval());
        therapyDB.setHour(calendarRecord.get(Calendar.HOUR_OF_DAY));
        therapyDB.setMinute(calendarRecord.get(Calendar.MINUTE));
        therapyDB.setName(txtTherapy.getText().toString());
        therapyDB.setMessage(product.getMessage());
        therapyDB.setTime_millis(calendarRecord.getTimeInMillis());

        if (therapyDB.insertRecord()) {

            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            intent.putExtra("id",id);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, therapyDB.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            long interval = milDay * product.getInterval();

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendarRecord.getTimeInMillis(),interval,pendingIntent);

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarRecord.getTimeInMillis(), pendingIntent);
//            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarRecord.getTimeInMillis(), pendingIntent);
//            } else {
//                alarmManager.set(AlarmManager.RTC_WAKEUP, calendarRecord.getTimeInMillis(), pendingIntent);
//            }

            Toast.makeText(this, "Schedule set", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Unable to set schedule.", Toast.LENGTH_SHORT).show();
        }
        therapyDB.close();
    }
}
