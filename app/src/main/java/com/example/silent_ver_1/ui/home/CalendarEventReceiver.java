package com.example.silent_ver_1.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CalendarEventReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("CalendarEventReceiver", "Calendar changed (or the phone just booted) "+intent.getAction()+" , "+context.toString());



    }
}
