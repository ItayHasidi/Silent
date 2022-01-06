package com.example.silent_ver_1.CalendarAssets;

import static android.content.ContentValues.TAG;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.silent_ver_1.UserHolder;
import com.example.silent_ver_1.databinding.FragmentHomeBinding;
import com.example.silent_ver_1.ui.home.AlarmBroadcastReceiver;
import com.example.silent_ver_1.ui.home.MainAdapter;
import com.example.silent_ver_1.ui.premium.FilterModel;
import com.example.silent_ver_1.ui.user.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public final class SyncCalendar extends AppCompatActivity {

    private static long todayStartMilli, todayEndMilli;
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
        Log.i(TAG, "User: "+user);
        /* Getting user info from the database */
        user = UserHolder.getUser();
        Log.i(TAG, "User: "+user);
    }

    /**
     * Creates a query to the OS that receives all the calendar events within the time-span that was sent.
     * @param uri
     * @param context
     * @param arrayList
     */
    private static void getEventsOfTheDay(Uri.Builder uri, Context context, ArrayList<CalendarEventModel> arrayList){
        user = UserHolder.getUser();
        Uri eventsUri = uri.build();
        Cursor cursor = null;

        cursor = context.getContentResolver().query(eventsUri, null, null, null, CalendarContract.Instances.DTSTART + " ASC");
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


                boolean toMute = false;
                if(user.isPremium()){
                    for(FilterModel f : UserHolder.getUser().getFilters()){
                        if(title.contains(f.getFilter())){
                            toMute = true;
                        }
                    }
                }

                CalendarEventModel model = new CalendarEventModel(start, end, desc, title, id, toMute);
                arrayList.add(model);
                Intent alarmIntent = new Intent(context, AlarmBroadcastReceiver.class);
                alarmIntent.setData(Uri.parse("custom://" + id+"s"));
                alarmIntent.setData(Uri.parse(id));

                alarmIntent.setAction("start");
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

                PendingIntent displayIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, alarmIntent, 0);

                alarmManager.set(AlarmManager.RTC_WAKEUP, start.getTime(), displayIntent);

                alarmIntent.setData(Uri.parse("custom://" +id+ "e"));
                alarmIntent.setAction("end");
                alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

                displayIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, alarmIntent, 0);

                alarmManager.set(AlarmManager.RTC_WAKEUP, end.getTime(), displayIntent);
            }
        }
        cursor.close();
        Log.i(TAG, "User: "+user);
        user.setEvents(arrayList, true);
    }

    /**
     * Gets the starting time of the query in milliseconds and calculates the end time(within 24 hours from starting time),
     * creates a query.
     * @param milliseconds
     * @param context
     * @param arrayList
     */
    public static void getEventsOfTheDay(long milliseconds, Context context, ArrayList<CalendarEventModel> arrayList){
        todayStartMilli = milliseconds;
        todayEndMilli = todayStartMilli + 86400000; // 86 400 000 is 1 day in milliseconds

        Uri.Builder uri = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(uri, todayStartMilli);
        ContentUris.appendId(uri, todayEndMilli);

        getEventsOfTheDay(uri, context, arrayList);
    }

    /**
     * Translates a date to milliseconds and calculates the end time(within 24 hours from starting time),
     * creates a query.
     * @param year
     * @param month
     * @param day
     * @param context
     * @param arrayList
     * @throws ParseException
     */
    public static void getEventsOfTheDay(int year, int month, int day, Context context, ArrayList<CalendarEventModel> arrayList) throws ParseException {
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

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date dateStart = sdf.parse(startDate);
            Date dateEnd = sdf.parse(endDate);
            long millisStart = dateStart.getTime() + 7200000;// GMT+02:00
            long millisEnd = dateEnd.getTime() + 7200000;

            ContentUris.appendId(uri, millisStart);
            ContentUris.appendId(uri, millisEnd);

        }
        getEventsOfTheDay(uri, context, arrayList);
    }

    /**
     * Calculates the time of day 00:00:00 in millisecond of this day.
     * @return
     */
    private static long getStartOfDayInMillis() {
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
    private static long getEndOfDayInMillis() {
        // Add one day's time to the beginning of the day.
        // 24 hours * 60 minutes * 60 seconds * 1000 milliseconds = 1 day
        return getStartOfDayInMillis() + (24 * 60 * 60 * 1000 - 1);
    }

    /**
     * Deletes from the database all the events that have already ended.
     * @param currTime
     */
    public static void deletePastEvents(long currTime){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/");
        if(currUser == null){
            currUser =FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        DatabaseReference myRef = database.getReference(currUser);
        Query query = myRef.child("/Events");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    if(snap.exists()){
                        CalendarEventModel c = snap.getValue(CalendarEventModel.class);
                        if(c.getEndDate().getTime() < currTime){
                            snap.getRef().removeValue();
                        }
                        boolean found = false;
                        for(CalendarEventModel calendarEventModel : user.getEvents()){
                            if(c.getId() == calendarEventModel.getId()){
                                found = true;
                                break;
                            }
                        }
                        if(!found){
                            snap.getRef().removeValue();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
