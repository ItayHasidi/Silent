package com.example.silent_ver_1.ui.settings;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.silent_ver_1.R;
import com.example.silent_ver_1.databinding.FragmentSettingsBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {
    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;
    private Button silentBtn;
    private int mode;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSettings;
        settingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        final AudioManager mode = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        this.mode = mode.getRingerMode();
        Log.d(TAG, "phone mode: "+this.mode);

        silentBtn = root.findViewById(R.id.silentBtn);
        silentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AudioManager.RINGER_MODE_NORMAL == mode.getRingerMode()) {
//                    mode.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    mode.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                }
                else{
                    mode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
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
}
