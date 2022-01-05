package com.example.silent_ver_1.ui.syncoptions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.silent_ver_1.R;
import com.example.silent_ver_1.databinding.FragmentSyncOptionsBinding;
import com.example.silent_ver_1.ui.premium.FilterEditMessage;


public class SyncOptionsFragment extends Fragment {
    private SyncOptionsViewModel syncOptionsViewModel;
    private FragmentSyncOptionsBinding binding;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSyncOptionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        syncOptionsViewModel =  new ViewModelProvider(this).get(SyncOptionsViewModel.class);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
