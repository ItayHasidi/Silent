package com.example.silent_ver_1.ui.premium;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.silent_ver_1.R;

public class GetCalls extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_calls);
        if(ContextCompat.checkSelfPermission(GetCalls.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(GetCalls.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
        }else{

        }

    }
}