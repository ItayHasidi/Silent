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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.silent_ver_1.R;
import com.example.silent_ver_1.ui.home.MainAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SyncCalendar extends AppCompatActivity {

    private long todayStartMilli, todayEndMilli;
    private final ArrayList<CalendarEventModel> arrayList = new ArrayList<>();
    private String currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sync_calendar);
    }

//        mainAdapter.notifyItemRangeChanged(0, mainAdapter.getItemCount());
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


    /**
     * This function will handle each event's mute functions
     * @param view the view
     */
    public void switchMute(View view){

    }

}
