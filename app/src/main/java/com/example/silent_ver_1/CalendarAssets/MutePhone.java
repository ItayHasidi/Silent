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


    /**
     * A constructor for the class MutePhone.
     * @param context
     */
    public MutePhone(Context context){
        this.context = context;
    }

    /**
     * Sets the phone to Do not disturb mode.
     */
    public void setSilent(){
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        try {
            if(Settings.Global.getInt(context.getContentResolver(), "zen_mode") == 0) {
                changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_NONE);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the phone off of Do not disturb mode.
     */
    public void setRegular(){
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_ALL);
    }


    /**
     * Verifies the version of operation system and switches between the two modes (DNT).
     * @param interruptionFilter
     */
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
