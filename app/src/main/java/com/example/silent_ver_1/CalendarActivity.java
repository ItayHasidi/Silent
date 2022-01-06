package com.example.silent_ver_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.silent_ver_1.CalendarAssets.CalendarEventModel;
import com.example.silent_ver_1.ui.home.MainAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    private static final String TAG = "CalendarActivity";
    private ArrayList<CalendarEventModel> arrayList = new ArrayList<>();
    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private int curPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        recyclerView = findViewById(R.id.recyclerViewEvents);
        checkPermission();
        getCalendarNames();
        getEventsOfTheDay();
    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(CalendarActivity.this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CalendarActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getCalendarNames();
            getEventsOfTheDay();
        }else{
            Toast.makeText(CalendarActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
            checkPermission();
        }
    }


    public void getCalendarNames(){
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null,null);
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                int str1 = cursor.getColumnIndex(CalendarContract.Calendars.NAME);
                String nameOfCal = cursor.getString(str1);
            }
        }
        cursor.close();
    }

    /**
     * Creates a query to the OS that receives all the calendar events within the time-span that was sent.
     */
    public void getEventsOfTheDay(){
        Uri.Builder uri = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(uri, getStartOfDayInMillis());
        ContentUris.appendId(uri, getEndOfDayInMillis());
        Uri eventsUri = uri.build();
        Cursor cursor = null;
        // gets all the events of the day
        cursor = getContentResolver().query(eventsUri, null, null,
                null, CalendarContract.Instances.DTSTART + " ASC");
        // iterates through all th events and prints them
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                int titleCur = cursor.getColumnIndex(CalendarContract.Instances.TITLE);
                String title = cursor.getString(titleCur);
                int descCur = cursor.getColumnIndex(CalendarContract.Instances.DESCRIPTION);
                String desc = cursor.getString(descCur);
                int idCur = cursor.getColumnIndex(CalendarContract.Instances.EVENT_ID);
                String id = cursor.getString(idCur);
                int startDateCur = cursor.getColumnIndex(CalendarContract.Instances.BEGIN);
                String startDate = cursor.getString(startDateCur);
                Date start = new Date(Long.parseLong(startDate));
                int endDateCur = cursor.getColumnIndex(CalendarContract.Instances.END);
                String endDate = cursor.getString(endDateCur);
                Date end = new Date(Long.parseLong(endDate));
                CalendarEventModel model = new CalendarEventModel(start, end, desc, title, id);
                arrayList.add(model);

            }
        }
        cursor.close();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainAdapter = new com.example.silent_ver_1.ui.home.MainAdapter(/*this,*/ arrayList);
        recyclerView.setAdapter(mainAdapter);
        mainAdapter.setOnItemClickListener(new com.example.silent_ver_1.ui.home.MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                arrayList.get(position).setToMute(!arrayList.get(position).isToMute());
                curPosition = position;
            }
        });
    }

    /**
     * Calculates the time of day 00:00:00 in millisecond of this day.
     * @return
     */
    public long getStartOfDayInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * Calculates the time of day 00:00:00 in millisecond of next day.
     * @return
     */
    public long getEndOfDayInMillis() {
        // Add one day's time to the beginning of the day.
        // 24 hours * 60 minutes * 60 seconds * 1000 milliseconds = 1 day
        return getStartOfDayInMillis() + (24 * 60 * 60 * 1000 - 1);
    }
}