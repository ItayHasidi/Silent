package com.example.silent_ver_1.CalendarAssets;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.silent_ver_1.R;
import com.example.silent_ver_1.UserHolder;
import com.example.silent_ver_1.databinding.FragmentHomeBinding;
import com.example.silent_ver_1.ui.home.MainAdapter;
import com.example.silent_ver_1.ui.user.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SyncCalendar extends AppCompatActivity {

    private static long todayStartMilli, todayEndMilli;
    private static ArrayList<CalendarEventModel> arrayList = new ArrayList<>();
    private static String currUser;
    private static UserModel user;
    private static FragmentHomeBinding binding;
    private static CalendarView calendarView;
    private static MainAdapter mainAdapter;
    private static RecyclerView recyclerView;
    private static int curPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = UserHolder.getUser();
//        setContentView(R.layout.activity_sync_calendar);
    }

    public static void getEventsOfTheDay(int year, int month, int day, Activity activity) throws ParseException {
        View root = binding.getRoot();
        Uri.Builder uri = CalendarContract.Instances.CONTENT_URI.buildUpon();
        if(year == 0){
            ContentUris.appendId(uri, getStartOfDayInMillis());
            ContentUris.appendId(uri, getEndOfDayInMillis());
        }
        else{
            String dayStr = day+"";
            String monthStr = month+"";
            if(day < 10){
                dayStr = "0"+dayStr;
            }
            if(month < 10){
                monthStr = "0"+monthStr;
            }

            String startDate = dayStr+"."+monthStr+"."+year+" 00:00";
            String endDate = dayStr+"."+monthStr+"."+year+" 23:59";

//            Log.i(TAG, "Calendar: Date: "+startDate+" , "+endDate);

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date dateStart = sdf.parse(startDate);
            Date dateEnd = sdf.parse(endDate);
            long millisStart = dateStart.getTime() + 7200000;// GMT+02:00
            long millisEnd = dateEnd.getTime() + 7200000;

            ContentUris.appendId(uri, millisStart);
            ContentUris.appendId(uri, millisEnd);

        }

        Uri eventsUri = uri.build();
        Cursor cursor = null;

        cursor = activity.getContentResolver().query(eventsUri, null, null, null, CalendarContract.Instances.DTSTART + " ASC");
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

                boolean toMute = true;
                for(CalendarEventModel ev : user.getEvents()) {
                    if(ev.getId() == Integer.parseInt(id)){
                        if (ev != null) {
                            toMute = ev.isToMute();
                        }
                    }
                }

                CalendarEventModel model = new CalendarEventModel(start, end, desc, title, id, toMute);
                arrayList.add(model);
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/");
                String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference myRef = database.getReference(currUser+"/Events/"+model);

            }
        }
        cursor.close();
        user.setEvents(arrayList, true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        mainAdapter = new com.example.silent_ver_1.ui.home.MainAdapter(activity, arrayList);
        recyclerView.setAdapter(mainAdapter);
        mainAdapter.setOnItemClickListener(new com.example.silent_ver_1.ui.home.MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                arrayList.get(position).setToMute(!arrayList.get(position).isToMute());
                curPosition = position;
            }
        });

    }



    public static long getStartOfDayInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getEndOfDayInMillis() {
        // Add one day's time to the beginning of the day.
        // 24 hours * 60 minutes * 60 seconds * 1000 milliseconds = 1 day
        return getStartOfDayInMillis() + (24 * 60 * 60 * 1000 - 1);
    }

/*
    public void getEventsOfTheDay(long todayMilli) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/");
        currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        this.todayStartMilli = todayMilli;
        this.todayEndMilli = this.todayStartMilli + 86400000; // 86 400 000 is 1 day in milliseconds

        Uri.Builder uri = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(uri, this.todayStartMilli);
        ContentUris.appendId(uri, this.todayEndMilli);



        Uri eventsUri = uri.build();
        Log.i(TAG, "Sync Log: "+this.todayStartMilli+" , "+todayEndMilli+" , "+ eventsUri);
        Cursor cursor = null;
    //        Log.i(TAG,"event:"+ uri.toString());
        // gets all the events of the day

        cursor = getBaseContext().getContentResolver().query(eventsUri, null, null,
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

    //                Log.i(TAG,"event:"+ title + " , "+ start+" , "+end);

                CalendarEventModel model = new CalendarEventModel(start, end, desc, title, id);
                arrayList.add(model);

                DatabaseReference myRef = database.getReference(currUser+"/Events/"+model);

    //                getDate();
    //                Log.i(TAG,"event:"+ title + " , "+ startMin+" , "+endMin+" , "+desc+" , "+startDate+" , "+endDate);
    //                Log.i(TAG, "event: "+model.toString());
            }
        }
        cursor.close();
    }
*/



}
