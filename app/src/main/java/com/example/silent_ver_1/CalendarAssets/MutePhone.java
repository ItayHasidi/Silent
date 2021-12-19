package com.example.silent_ver_1.CalendarAssets;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.provider.Settings;

public class MutePhone {
    private NotificationManager notificationManager;
    private Context context;

    public MutePhone(Context context){
        this.context = context;
    }

    public void setSilent(){
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        try {
            if(Settings.Global.getInt(context.getContentResolver(), "zen_mode") == 0) {
                changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_NONE);
            }
            else{
                changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_ALL);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void changeInterruptionFiler(int interruptionFilter){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){ // If api level minimum 23
            if(notificationManager.isNotificationPolicyAccessGranted()){
                notificationManager.setInterruptionFilter(interruptionFilter);
            }
            else {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                context.startActivity(intent);
            }
        }
    }
}
