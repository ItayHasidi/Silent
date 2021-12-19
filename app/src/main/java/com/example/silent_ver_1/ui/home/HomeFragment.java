package com.example.silent_ver_1.ui.home;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.silent_ver_1.CalendarActivity;
import com.example.silent_ver_1.CalendarAssets.CalendarEventModel;
import com.example.silent_ver_1.R;
import com.example.silent_ver_1.databinding.FragmentHomeBinding;

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


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        calendarView = root.findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener((calendarView, year, month, day) -> {
//            Log.i(TAG, "Calendar: "+new Date(calendarView.getDate()).toString());
//            Log.i(TAG, "Calendar: "+year+" , "+(month+1)+" , "+day);

            try {
                arrayList.clear();
                getEventsOfTheDay(year, month+1, day);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        });
//        Log.i(TAG, "Calendar: "+new Date(calendarView.getDate()).toString());

        recyclerView = root.findViewById(R.id.recyclerViewEvents);
//        recyclerView.invalidate();



//        checkPermission();
//        getCalendarNames();

        try {
            arrayList.clear();
            getEventsOfTheDay(0,0,0);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return root;
    }

    @Override
    public void onDestroyView() {
//        arrayList.clear();
//        mainAdapter.notifyDataSetChanged();
        super.onDestroyView();
        binding = null;
    }

//    @Override
//    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//
//        calendarView.setHeaderTextAppearance(R.style.AppTheme);
//
//        List<Event> event =  map.get(date);
//        if(event!=null && event.size()>0) {
//            adapter.addItems(event);
//        }else {
//            adapter.clear();
//        }
//    }


    public void getEventsOfTheDay(int year, int month, int day) throws ParseException {

//        mainAdapter.notifyItemRangeChanged(0, mainAdapter.getItemCount());

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
            long millisStart = dateStart.getTime() + 7200000; // GMT+02:00
            long millisEnd = dateEnd.getTime() + 7200000;

            ContentUris.appendId(uri, millisStart);
            ContentUris.appendId(uri, millisEnd);

//            Log.i(TAG, "Calendar: "+millisStart+ " , "+millisEnd);
        }

        Uri eventsUri = uri.build();
        Cursor cursor = null;
//        Log.i(TAG,"event:"+ uri.toString());
        // gets all the events of the day
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

//                Log.i(TAG,"event:"+ title + " , "+ start+" , "+end);

                CalendarEventModel model = new CalendarEventModel(start, end, desc, title, id);
                arrayList.add(model);
//                getDate();
//                Log.i(TAG,"event:"+ title + " , "+ startMin+" , "+endMin+" , "+desc+" , "+startDate+" , "+endDate);
//                Log.i(TAG, "event: "+model.toString());
            }
        }
        cursor.close();
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

    /**
     * This function will handle each event's mute functions
     * @param view the view
     */
    public void switchMute(View view){

    }



}