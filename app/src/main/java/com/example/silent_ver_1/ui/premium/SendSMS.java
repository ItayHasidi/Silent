package com.example.silent_ver_1.ui.premium;

import android.telephony.SmsManager;

public class SendSMS {

    /**
     * Sends a SMS message to the given phoneNumber with the given message.
     * @param phoneNumber
     * @param msg
     */
    public void send(String phoneNumber, String msg){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, msg, null, null);
    }
}