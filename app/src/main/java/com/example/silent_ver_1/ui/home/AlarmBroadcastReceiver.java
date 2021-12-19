package com.example.silent_ver_1.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.silent_ver_1.CalendarAssets.MutePhone;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        MutePhone mutePhone = new MutePhone(context);
        mutePhone.setSilent();

//        throw new UnsupportedOperationException("Not yet implemented");
    }
}