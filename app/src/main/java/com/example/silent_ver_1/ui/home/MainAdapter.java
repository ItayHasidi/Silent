package com.example.silent_ver_1.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.silent_ver_1.CalendarAssets.CalendarEventModel;
import com.example.silent_ver_1.CalendarAssets.Time;
import com.example.silent_ver_1.R;
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

        com.example.silent_ver_1.CalendarAssets.CalendarEventModel model = arrayList.get(position);
//        Log.i(TAG, "model: calendar "+holder.tvTitle);
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
    }


    public void refreshView(int position){
        notifyItemChanged(position);
    }


        @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDesc, tvTime, tvDate;
        Switch muteSwitch;
        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvDate = itemView.findViewById(R.id.tvDate);
//            Log.i(TAG, "model: calendar init"+tvTitle+" , "+tvDesc+" , "+tvTime);

            muteSwitch = itemView.findViewById(R.id.toMute);
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
