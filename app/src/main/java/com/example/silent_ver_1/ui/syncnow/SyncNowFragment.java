package com.example.silent_ver_1.ui.syncnow;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.silent_ver_1.CalendarAssets.CalendarEventModel;
import com.example.silent_ver_1.CalendarAssets.SyncCalendar;
import com.example.silent_ver_1.R;
import com.example.silent_ver_1.databinding.FragmentSyncNowBinding;
import com.example.silent_ver_1.ui.home.AlarmBroadcastReceiver;
import com.example.silent_ver_1.ui.home.MainAdapter;
import com.example.silent_ver_1.ui.premium.SyncCalendarActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SyncNowFragment extends Fragment {
    private SyncNowViewModel syncNowViewModel;
    private FragmentSyncNowBinding binding;
    private Button syncBtn;

    private long todayStartMilli, todayEndMilli;
//    private final ArrayList<CalendarEventModel> arrayList = new ArrayList<>();
    private String currUser;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        syncNowViewModel = new ViewModelProvider(this).get(SyncNowViewModel.class);
        binding = FragmentSyncNowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        syncBtn = root.findViewById(R.id.syncBtn);
        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long time = System.currentTimeMillis();
//                SyncCalendarActivity syncCalendar = new SyncCalendarActivity();
                getEventsOfTheDaySync(time);
                Log.d(TAG, "Sync Log finished");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getEventsOfTheDaySync(long todayMilli) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/");
        currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        this.todayStartMilli = todayMilli;
        this.todayEndMilli = this.todayStartMilli + 86400000; // 86 400 000 is 1 day in milliseconds

        Uri.Builder uri = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(uri, this.todayStartMilli);
        ContentUris.appendId(uri, this.todayEndMilli);

        Uri eventsUri = uri.build();

        Cursor cursor = getActivity().getContentResolver().query(eventsUri, null, null, null, CalendarContract.Instances.DTSTART + " ASC");
        Log.d(TAG, "Sync Log: count "+cursor.getCount());
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

                Log.i(TAG,"event:"+ title + " , "+ start+" , "+end);

                CalendarEventModel model = new CalendarEventModel(start, end, desc, title, id);
                Log.d(TAG, "Sync Log: "+model.toString());

                DatabaseReference myRef = database.getReference(currUser+"/Events/"+model.getId());
                myRef.setValue(model);

                Intent alarmIntent = new Intent(getActivity().getApplicationContext(), AlarmBroadcastReceiver.class);
                alarmIntent.setData(Uri.parse("custom://" + id+"s"));
                alarmIntent.setAction(id+"s");
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

                PendingIntent displayIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, alarmIntent, 0);

                alarmManager.set(AlarmManager.RTC_WAKEUP, start.getTime(), displayIntent);

                alarmIntent.setData(Uri.parse("custom://" + id+"e"));
                alarmIntent.setAction(id+"e");
                alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

                displayIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, alarmIntent, 0);

                alarmManager.set(AlarmManager.RTC_WAKEUP, end.getTime(), displayIntent);
            }
        }
        cursor.close();
    }
}