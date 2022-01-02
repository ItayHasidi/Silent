package com.example.silent_ver_1.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.silent_ver_1.CalendarAssets.CalendarEventModel;
import com.example.silent_ver_1.CalendarAssets.Time;
import com.example.silent_ver_1.R;
import com.example.silent_ver_1.UserHolder;
import com.example.silent_ver_1.ui.user.UserModel;
import com.google.android.material.timepicker.TimeFormat;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private static final String TAG = "MainAdapter";
    Activity activity;
    ArrayList<CalendarEventModel> arrayList;

    private OnItemClickListener mListener;
    private UserModel user;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public MainAdapter(Activity activity, ArrayList<CalendarEventModel> arrayList){
        this.activity = activity;
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        user = UserHolder.getUser();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_event, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshView(position);
            }
        });

        CalendarEventModel model = arrayList.get(position);
        holder.tvTitle.setText(model.getTitle());

        Date start = new Date(model.getStartDate().getTime());
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+02:00"));
        String startDate = dateFormat.format(start);
        String startTime = timeFormat.format(start);

        Date end = new Date(model.getEndDate().getTime());
        String endDate = dateFormat.format(end);
        String endTime = timeFormat.format(end);

        Log.i(TAG,  "date:: "+startDate+" , "+endDate);

        holder.tvTime.setText(startTime+" - "+endTime);
        holder.tvDate.setText(startDate+" - "+endDate);
        if(model.getDescription() != null){
            holder.tvDesc.setText("No description.");
        }
        else{
            holder.tvDesc.setText(model.getDescription());
        }
        ArrayList<CalendarEventModel> events = user.getEvents();
        for(CalendarEventModel event : events){
            if(event.getId() == model.getId()){
                holder.muteSwitch.setChecked(event.isToMute());
            }
        }
//        holder.muteSwitch.setChecked(user.getEvents().get(model.getId()).isToMute());

        // Here we update the toMute field of the event that the user has changed
        // We draw all the events from the user and find the relevant one to change
        holder.muteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ArrayList<CalendarEventModel> events = user.getEvents();
                for(CalendarEventModel event : events){
                    Log.i(TAG, "Adapter: "+event);
                    if(event.getId() == model.getId()){
                        event.setToMute(b);
                        user.setMuteEvent(event, true);
                    }
                }
//                user.setEvent(events, true);
            }
        });
    }

    public void refreshView(int position){
        notifyItemChanged(position);
    }

        @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private boolean isToMute(String str){
        ArrayList<CalendarEventModel> events = user.getEvents();
        for(CalendarEventModel event : events){
            Log.i(TAG, "compare: "+str+ " , "+event.getTitle());
            if(event.getTitle().equals(str)){
                return event.isToMute();
            }
        }
        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDesc, tvTime, tvDate;
        private Switch muteSwitch;
        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvDate = itemView.findViewById(R.id.tvDate);

            muteSwitch = itemView.findViewById(R.id.toMute);



//            // Here we update the toMute field of the event that the user has changed
//            // We draw all the events from the user and find the relevant one to change
//            muteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    ArrayList<CalendarEventModel> events = user.getEvents();
//                    for(CalendarEventModel event : events){
//                        if(event.getTitle().equals(tvTitle.getText())){
//                            event.setToMute(b);
//                        }
//                    }
//                    user.setEvents(events, true);
//                }
//            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
