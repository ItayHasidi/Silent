package com.example.silent_ver_1.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.silent_ver_1.CalendarAssets.CalendarEventModel;
import com.example.silent_ver_1.CalendarAssets.MutePhone;
import com.example.silent_ver_1.UserHolder;
import com.example.silent_ver_1.ui.syncnow.SyncNowFragment;
import com.example.silent_ver_1.ui.user.UserModel;

import java.util.ArrayList;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmBroadcastReceiver";
    private UserModel user;

    @Override
    public void onReceive(Context context, Intent intent) {
        user = UserHolder.getUser();
        MutePhone mutePhone = new MutePhone(context);

        if(intent.getAction() == "start") {
            Log.i(TAG, "Alarm: "+String.valueOf(intent.getData()));
            if(isToMute(Integer.parseInt(String.valueOf(intent.getData())))) {
                user.setSilent(true);
                mutePhone.setSilent();
            }
        }
        if(intent.getAction() == "end") {
            user.setSilent(false);
            mutePhone.setRegular();
        }

        if(intent.getAction() == "sync") {
            new SyncNowFragment().getEventsOfTheDaySync( System.currentTimeMillis() );
        }
//        else{
//            user.setSilent(false);
//            mutePhone.setRegular();
//        }getEventsOfTheDaySync
    }

    private boolean isToMute(int id){
        //isToMute(Integer.parseInt(id))
        ArrayList<CalendarEventModel> events = user.getEvents();
        for(CalendarEventModel event : events){
            if(event.getId() == id){
                return event.isToMute();
            }
        }
        return false;
    }
}