package com.drei.renovarapp.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.drei.renovarapp.Services.AlarmService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

//        Intent startIntent = new Intent(context, RingtoneService.class);
//        context.startService(startIntent);

//        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        if (alarmUri == null) {
//            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        }
//        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
//        ringtone.play();
//
        //this will send a notification message
//        ComponentName comp = new ComponentName(context.getPackageName(),
//                AlarmService.class.getName());
//        startWakefulService(context, (intent.setComponent(comp)));
//        setResultCode(Activity.RESULT_OK);
//        Intent startIntent = new Intent(context, AlarmService.class);
//        context.startService(startIntent);
        Intent alarm = new Intent(context,AlarmService.class);
//        Toast.makeText(context, "" + intent.getIntExtra("id",0), Toast.LENGTH_SHORT).show();
        alarm.putExtra("id",intent.getIntExtra("id",0));
        context.startService(alarm);
    }
}