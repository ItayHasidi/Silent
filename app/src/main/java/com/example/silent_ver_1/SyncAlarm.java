package com.example.silent_ver_1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.silent_ver_1.ui.home.AlarmBroadcastReceiver;
import com.example.silent_ver_1.ui.user.UserModel;

import java.util.Calendar;

public final class SyncAlarm {

    private static final String TAG = "SyncAlarm";


    public SyncAlarm(){}

    /**
     * Creates a 'sync' alarm that sets off each day at 06:00 to sync the DB with the events that are saved on the users phone.
     * @param context
     */
    public static void createSyncAlarm(Context context){
        Intent alarmIntent = new Intent(context, AlarmBroadcastReceiver.class);

        alarmIntent.setData(Uri.parse("custom://" +"sync"));
        alarmIntent.setAction("sync");
        AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(context.ALARM_SERVICE);

        PendingIntent displayIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, alarmIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long t = calendar.getTimeInMillis();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,t,AlarmManager.INTERVAL_DAY,displayIntent);
    }
}
