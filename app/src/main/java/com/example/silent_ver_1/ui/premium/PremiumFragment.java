package com.example.silent_ver_1.ui.premium;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.silent_ver_1.R;
import com.example.silent_ver_1.databinding.FragmentPremiumBinding;
import com.example.silent_ver_1.ui.home.HomeFragment;
import com.example.silent_ver_1.ui.register.RegisterFragment;

public class PremiumFragment extends Fragment {


    private PremiumViewModel premiumViewModel;
    private FragmentPremiumBinding binding;
    private Button defMsgBtn, goToGetCalls;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        premiumViewModel =
                new ViewModelProvider(this).get(PremiumViewModel.class);

        binding = FragmentPremiumBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        defMsgBtn = root.findViewById(R.id.goToMesgSetBtn);
        defMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(R.id.container, ContactEditMessage.class));// עוברים מסך
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new ContactEditMessage()).commit();
                    Intent intent = new Intent(getActivity(), ContactEditMessage.class);
                    startActivity(intent);
            }
        });

//        goToGetCalls.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), ContactEditMessage.class);
//                startActivity(intent);
//            }
//        });

//        final TextView textView = binding.textPremium;
//        premiumViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
