package com.example.silent_ver_1.ui.home;

import android.app.Activity;
import android.content.ContentUris;
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
import android.widget.CalendarView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.silent_ver_1.CalendarActivity;
import com.example.silent_ver_1.CalendarAssets.CalendarEventModel;
import com.example.silent_ver_1.CalendarAssets.SyncCalendar;
import com.example.silent_ver_1.R;
import com.example.silent_ver_1.UserHolder;
import com.example.silent_ver_1.databinding.FragmentHomeBinding;
import com.example.silent_ver_1.ui.user.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class HomeFragment extends Fragment /*implements OnDateSelectedListener*/{

    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
//    public Button btn;
    private CalendarView calendarView;

    private final ArrayList<CalendarEventModel> arrayList = new ArrayList<>();
    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private int curPosition = -1;
    private UserModel user;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user = UserHolder.getUser();

        calendarView = root.findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener((calendarView, year, month, day) -> {
            try {
                arrayList.clear();
//                SyncCalendar.getEventsOfTheDay(year, month+1, day, getActivity());
                getEventsOfTheDay(year, month+1, day);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        });

        recyclerView = root.findViewById(R.id.recyclerViewEvents);

        try {
            arrayList.clear();
//            SyncCalendar.getEventsOfTheDay(0,0,0, getActivity());
            getEventsOfTheDay(0,0,0 );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void getEventsOfTheDay(int year, int month, int day) throws ParseException {
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

        cursor = getActivity().getContentResolver().query(eventsUri, null, null, null, CalendarContract.Instances.DTSTART + " ASC");
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
//        user.setEvents(arrayList, true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        mainAdapter = new com.example.silent_ver_1.ui.home.MainAdapter(getActivity(), arrayList);
        recyclerView.setAdapter(mainAdapter);
        mainAdapter.setOnItemClickListener(new com.example.silent_ver_1.ui.home.MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                arrayList.get(position).setToMute(!arrayList.get(position).isToMute());
                curPosition = position;
            }
        });

    }


    public long getStartOfDayInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public long getEndOfDayInMillis() {
        // Add one day's time to the beginning of the day.
        // 24 hours * 60 minutes * 60 seconds * 1000 milliseconds = 1 day
        return getStartOfDayInMillis() + (24 * 60 * 60 * 1000 - 1);
    }
}