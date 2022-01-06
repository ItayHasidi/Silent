package com.example.silent_ver_1.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.example.silent_ver_1.CalendarAssets.CalendarEventModel;
import com.example.silent_ver_1.CalendarAssets.MutePhone;
import com.example.silent_ver_1.CalendarAssets.SyncCalendar;
import com.example.silent_ver_1.SyncAlarm;
import com.example.silent_ver_1.UserHolder;
import com.example.silent_ver_1.ui.user.UserModel;

import java.util.ArrayList;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmBroadcastReceiver";
    private UserModel user;

    /**
     * Receives three types of broadcasts: 'start', 'end' and 'sync'.
     * start - sets the phone to DNT(Do Not Disturb) mode.
     * end - sets the phone off of DNT mode.
     * sync - gets all the events from the OS saves it to the DB and creates two new alarms for each event - 'start' and 'end'.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        user = UserHolder.getUser();
        MutePhone mutePhone = new MutePhone(context);

        if(intent.getAction() == "start") {
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
            SyncCalendar.getEventsOfTheDay(System.currentTimeMillis(), context, new ArrayList<>());
        }
    }

    /**
     * Checks if an event should be set to DNT mode by id.
     * @param id
     * @return
     */
    private boolean isToMute(int id){
        ArrayList<CalendarEventModel> events = user.getEvents();
        for(CalendarEventModel event : events){
            if(event.getId() == id){
                return event.isToMute();
            }
        }
        return false;
    }
}