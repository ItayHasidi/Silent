package com.example.silent_ver_1.ui.premium;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.silent_ver_1.CalendarAssets.CalendarEventModel;
import com.example.silent_ver_1.R;
import com.example.silent_ver_1.UserHolder;
import com.example.silent_ver_1.ui.home.MainAdapter;
import com.example.silent_ver_1.ui.user.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class SyncCalendarActivity extends AppCompatActivity {

    private long todayStartMilli, todayEndMilli;
    private final ArrayList<CalendarEventModel> arrayList = new ArrayList<>();
    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private int curPosition = -1;
    private String currUser;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_calendar);
        user = UserHolder.getUser();


    }

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
//        Cursor cursor = null;
        //        Log.i(TAG,"event:"+ uri.toString());
        // gets all the events of the day

        Cursor cursor = getBaseContext().getContentResolver().query(eventsUri, null, null, null, CalendarContract.Instances.DTSTART + " ASC");
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
                mainAdapter = new MainAdapter(/*this,*/ arrayList);
                recyclerView.setAdapter(mainAdapter);

                DatabaseReference myRef = database.getReference(currUser+"/Events/"+model);

              }
        }
        cursor.close();
    }
}