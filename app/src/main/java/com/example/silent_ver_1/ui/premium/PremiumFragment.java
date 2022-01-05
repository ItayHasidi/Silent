package com.example.silent_ver_1.ui.premium;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.silent_ver_1.MainActivity;
import com.example.silent_ver_1.R;
import com.example.silent_ver_1.UserHolder;
import com.example.silent_ver_1.databinding.FragmentPremiumBinding;
import com.example.silent_ver_1.ui.user.UserModel;

public class PremiumFragment extends Fragment {


    private PremiumViewModel premiumViewModel;
    private FragmentPremiumBinding binding;
    private Button defMsgBtn, goToGetCalls,goToFilters;
    private UserModel user;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        premiumViewModel = new ViewModelProvider(this).get(PremiumViewModel.class);
        binding = FragmentPremiumBinding.inflate(inflater, container, false);
        user = UserHolder.getUser();
        boolean premium = user.isPremium();

        View root = binding.getRoot();
        goToFilters = (Button)root.findViewById(R.id.buttonChangeFilter);
        goToFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(premium){
                    Intent intent = new Intent(getActivity(), FilterEditMessage.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(),"You have not premium membership!", Toast.LENGTH_LONG).show();
                }
            }
        });
        defMsgBtn = root.findViewById(R.id.goToMesgSetBtn);
        defMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(premium){
                        Intent intent = new Intent(getActivity(), ContactEditMessage.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getContext(),"You have not premium membership!", Toast.LENGTH_LONG).show();
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
