package com.example.silent_ver_1.ui.syncnow;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.silent_ver_1.R;
import com.example.silent_ver_1.databinding.FragmentSyncNowBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SyncNowFragment extends Fragment {
    private SyncNowViewModel syncNowViewModel;
    private FragmentSyncNowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        syncNowViewModel =
                new ViewModelProvider(this).get(SyncNowViewModel.class);

        binding = FragmentSyncNowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSyncNow;
        syncNowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
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
