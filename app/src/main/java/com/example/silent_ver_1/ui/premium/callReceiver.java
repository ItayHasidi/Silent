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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.silent_ver_1.NavDrawer;
import com.example.silent_ver_1.UserHolder;
import com.example.silent_ver_1.ui.user.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class callReceiver extends BroadcastReceiver {

    private UserModel user;
    private String defSMS = "Can't talk, call you back later.";
    public static String BROADCAST_ACTION = "android.permission.READ_CALL_LOG";
    private String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/");

    public String getDefSMS() {
        return defSMS;
    }

    public void setDefSMS(String defSMS) {
        this.defSMS = defSMS;
    }

    /**
     * Catches phone calls when the phone is in Silent mode and sends the designated SMS that was defined by the user.
     * If the user has not defined one for the calling contact, a default message will be sent.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive ( final Context context, Intent intent){
        user = UserHolder.getUser();

        if (user.isPremium() && user.isSilent() && intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            if (!intent.getExtras().containsKey(TelephonyManager.EXTRA_INCOMING_NUMBER)) {
            } else {
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Toast.makeText(context, "Call from: " + number, Toast.LENGTH_LONG).show();
                DatabaseReference myRef = database.getReference(currUser + "/Contacts/" + number);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            String msg = snapshot.getValue().toString();
                            new SendSMS().send(number, msg);
                        }
                        else if(number != (null)){
                            new SendSMS().send(number, defSMS);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        }
    }
}
