package com.example.silent_ver_1.ui.syncnow;

import static android.content.ContentValues.TAG;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
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

import com.example.silent_ver_1.CalendarAssets.SyncCalendar;
import com.example.silent_ver_1.R;
import com.example.silent_ver_1.databinding.FragmentSyncNowBinding;
import com.example.silent_ver_1.ui.premium.SyncCalendarActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SyncNowFragment extends Fragment {
    private SyncNowViewModel syncNowViewModel;
    private FragmentSyncNowBinding binding;
    private Button syncBtn;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        syncNowViewModel = new ViewModelProvider(this).get(SyncNowViewModel.class);
        binding = FragmentSyncNowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        syncBtn = root.findViewById(R.id.syncBtn);
        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long time = System.currentTimeMillis();
                SyncCalendarActivity syncCalendar = new SyncCalendarActivity();
                syncCalendar.getEventsOfTheDay(time);
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



}
