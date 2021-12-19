package com.example.silent_ver_1.ui.premium;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.silent_ver_1.NavDrawer;

public class callReceiver extends BroadcastReceiver {

    private String incomingNumber;

    public String getIncomingNumber() {
        return incomingNumber;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
//        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//        telephony.listen(new PhoneStateListener(){
//            @Override
//            public void onCallStateChanged(int state, String incomingNumber) {
//                super.onCallStateChanged(state, incomingNumber);
//                Log.i("callReceiver", "incomingNumber : "+incomingNumber);
//            }
//        },PhoneStateListener.LISTEN_CALL_STATE);


        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                System.out.println("incomingNumber : "+incomingNumber);
            }
        },PhoneStateListener.LISTEN_CALL_STATE);
    }

//    @Override
//    public void onReceive(final Context context, Intent intent) {
////        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//        String number = "aa ";
//        number += intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
////        number += telephonyManager.getLine1Number();
//
//        Toast.makeText(context, "caller number: "+number+" ....... ", Toast.LENGTH_LONG).show();
//        incomingNumber = number;
////        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
////        telephony.listen(new PhoneStateListener() {
////            @Override
////            public void onCallStateChanged(int state, String incomingNumber) {
////                super.onCallStateChanged(state, incomingNumber);
////                System.out.println("incomingNumber : " + incomingNumber);
////            }
////        }, PhoneStateListener.LISTEN_CALL_STATE);
////        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
////
////        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
////            String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
////            Toast.makeText(context, "caller number: "+number, Toast.LENGTH_LONG).show();
////            incomingNumber = number;
////        }
//    }
}
