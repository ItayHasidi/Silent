package com.example.silent_ver_1;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.silent_ver_1.ui.home.AlarmBroadcastReceiver;
import com.example.silent_ver_1.ui.home.CalendarEventReceiver;
import com.example.silent_ver_1.ui.user.UserModel;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.silent_ver_1.databinding.ActivityNavDrawerBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;

public class NavDrawer extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavDrawerBinding binding;
    private Button headerBtn;
    private TextView txtHeader;
    private String username;
    private FirebaseAuth mAuth;
    private UserModel user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = new UserModel();
        user = UserHolder.getUser();

        if(!user.isHasSyncAlarm()){
            Toast.makeText(this, "Created", Toast.LENGTH_LONG).show();
            user.setHasSyncAlarm(true);
            Intent alarmIntent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);

            alarmIntent.setAction("sync");
            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);

            PendingIntent displayIntent = PendingIntent.getBroadcast(getApplicationContext().getApplicationContext(), 0, alarmIntent, 0);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR, 22);
            calendar.set(Calendar.MINUTE, 33);
            calendar.set(Calendar.SECOND, 0);
            long t = calendar.getTimeInMillis();
            alarmManager.set(AlarmManager.RTC_WAKEUP, t, displayIntent);
        }

        binding = ActivityNavDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavDrawer.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_settings, R.id.nav_sync_now, R.id.nav_sync_options
        ,R.id.nav_premium, R.id.nav_user, R.id.nav_about)
                .setOpenableLayout(drawer)
                .build();

        View header = navigationView.getHeaderView(0);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        username = currentUser.getEmail();

        headerBtn = header.findViewById(R.id.userBtnHeader);
        txtHeader = header.findViewById(R.id.userTxtHeader);



        if(currentUser != null){
            headerBtn.setText("Logout");
            txtHeader.setText("User: "+username);
        }
        else{
            headerBtn.setText("Login");
            txtHeader.setText("Please login");
        }

        headerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUser != null){
                    FirebaseAuth.getInstance().signOut();
                }
                startActivity(new Intent(NavDrawer.this, LoginActivity.class));// עוברים מסך
            }
        });



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED);
        filter.addDataScheme("content");
        filter.addDataAuthority("com.android.calendar", null);
        registerReceiver(new CalendarEventReceiver(), filter);
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(NavDrawer.this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(NavDrawer.this, new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }
        if(ContextCompat.checkSelfPermission(NavDrawer.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(NavDrawer.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
        }
    }
}