package com.example.silent_ver_1.ui.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

//import com.example.silent_ver_1.databinding.FragmentLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment<FragmentLoginBinding> extends Fragment {
    private LoginViewModel LoginViewModel;
    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;
    private EditText mEmail, mPass, mConfPass;
    private Button mRegBtn;
    private TextView mLoginBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LoginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);

//        binding = FragmentLoginBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();

//        final TextView textView = binding.welcomeText;
//        LoginViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
