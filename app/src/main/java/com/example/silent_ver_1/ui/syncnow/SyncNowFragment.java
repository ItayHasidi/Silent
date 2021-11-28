package com.example.silent_ver_1.ui.syncnow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.silent_ver_1.databinding.FragmentSyncNowBinding;

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
