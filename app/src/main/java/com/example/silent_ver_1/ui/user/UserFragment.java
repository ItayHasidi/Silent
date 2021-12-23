package com.example.silent_ver_1.ui.user;

import static android.content.ContentValues.TAG;

import android.content.Intent;
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

import com.example.silent_ver_1.LoginActivity;
import com.example.silent_ver_1.MainActivity;
import com.example.silent_ver_1.NavDrawer;
import com.example.silent_ver_1.R;
import com.example.silent_ver_1.databinding.FragmentUserBinding;
import com.example.silent_ver_1.ui.settings.SettingsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserFragment extends Fragment {
    private SettingsViewModel settingsViewModel;
    private FragmentUserBinding binding;
    private String uid, email;
    private FirebaseAuth mAuth;
    private Button stateBtn, getDetailsBtn;
    private TextView uidText, emailText, textEmail, textPremium;
    private UserModel user;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
         this.user = new UserModel();
        user.getUser();

        ////

        getDetailsBtn = root.findViewById(R.id.getDetailsBtn);
        textEmail = root.findViewById(R.id.textEmail);
        textPremium = root.findViewById(R.id.textPremium);

        getDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textEmail.setText(user.getEmail());
                textPremium.setText("premium: "+user.isPremium());
            }
        });



        ////

        stateBtn = root.findViewById(R.id.button_state);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        email = currentUser.getEmail();

        uidText = root.findViewById(R.id.textView4);
        emailText = root.findViewById(R.id.textView);
        uidText.setText("User ID: "+uid);
        emailText.setText("Email: "+email);

        if(currentUser != null){
            stateBtn.setText("Logout");
        }
        else{
            stateBtn.setText("Login");
        }

        stateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUser != null){
                    FirebaseAuth.getInstance().signOut();
                }
                startActivity(new Intent(getActivity(), LoginActivity.class));// עוברים מסך
            }
        });

        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
