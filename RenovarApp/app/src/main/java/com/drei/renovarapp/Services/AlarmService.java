package com.drei.renovarapp.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.drei.renovarapp.Activities.CameraActivity;
import com.drei.renovarapp.R;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class AlarmService extends Service {
    private NotificationManager mNotificationManager;
    private String CHANNEL_ID = "ALARM_THERAPY";
    private int NOTIFICATION_ID = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendNotification("Time for your Therapy",intent.getIntExtra("id",0));
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendNotification(String msg,int id) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
        intent.putExtra("id",id);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("Time for your therapy.");
        bigText.setBigContentTitle("Therapy Alarm");
        bigText.setSummaryText("Alarm");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.logo_only);
        mBuilder.setContentTitle("Renovar");
        mBuilder.setContentText("Time for your therapy.");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mBuilder.setSound(alarmSound);
        mBuilder.addAction(R.drawable.ic_dna, "PERFORM THERAPY",
                pendingIntent).addAction(R.drawable.ic_access_time_black_24dp, "SNOOZE",
                pendingIntent);

        mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());
        Log.d("AlarmService", "Notification sent.");
    }
}
