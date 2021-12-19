package com.example.silent_ver_1.ui.settings;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.silent_ver_1.R;
import com.example.silent_ver_1.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {
    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;
    private Button silentBtn;
    private NotificationManager notificationManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSettings;
        settingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        silentBtn = root.findViewById(R.id.silentBtn);
        silentBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                try {
                    if(Settings.Global.getInt(getActivity().getContentResolver(), "zen_mode") == 0) {
                        changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_NONE);
                    }
                    else{
                        changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_ALL);
                    }
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    protected void changeInterruptionFiler(int interruptionFilter){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){ // If api level minimum 23
            if(notificationManager.isNotificationPolicyAccessGranted()){
                notificationManager.setInterruptionFilter(interruptionFilter);
            }
            else {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
            }
        }
    }
}
