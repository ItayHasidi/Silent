package com.example.silent_ver_1.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.silent_ver_1.MainActivity;
import com.example.silent_ver_1.R;
import com.example.silent_ver_1.databinding.FragmentRegisterBinding;
import com.example.silent_ver_1.ui.Login.LoginFragment;
import com.example.silent_ver_1.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {
    private RegisterViewModel registerViewModel;
    private FragmentRegisterBinding binding;
    private FirebaseAuth mAuth;
    private Button registerBtn;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        registerBtn = root.findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText mEmail, mPass, mConfPass;

                TextView mLoginBtn;
                mEmail = getView().findViewById(R.id.email);
                mPass = getView().findViewById(R.id.password);
                mConfPass = getView().findViewById(R.id.password2);
                mLoginBtn = getView().findViewById(R.id.log_in_Btn);

                String email = mEmail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();
                String confPass = mConfPass.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError(("Email Is required"));
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    mPass.setError(("Password Is required"));
                    return;
                }
                if(TextUtils.isEmpty(confPass)){
                    mConfPass.setError(("Password Confirmation Is required"));
                    return;
                }
                if(!pass.equals(confPass)){
                    mPass.setError(("Passwords are not the same"));
                    return;
                }
                if(pass.length() < 6){
                    mPass.setError(("Password Should be at least 6 characters long"));
                    return;
                }
//        Fragment fragment = new YourFragment();
//
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction transaction = fm.beginTransaction();
//        transaction.replace(R.id.contentFragment, fragment);
//        transaction.commit();

                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {// אם מצליח
                                    Toast.makeText(view.getContext(), "user created", Toast.LENGTH_LONG).show();// הודעה
//                            startActivity(new Intent(RegisterFragment.this, HomeFragment));// עוברים מסך
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();

                                }
                                else {// אם לא מצליח
                                    Toast.makeText(view.getContext(), "failed", Toast.LENGTH_LONG).show();// הודעה
                                }
                            }
                        });
            }
        });

        final TextView textView = binding.welcomeText;
        registerViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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

//    public void btnRegister(View view) {
//
//        EditText mEmail, mPass, mConfPass;
//
//        TextView mLoginBtn;
//        mEmail = getView().findViewById(R.id.email);
//        mPass = getView().findViewById(R.id.password);
//        mConfPass = getView().findViewById(R.id.password2);
//        mLoginBtn = getView().findViewById(R.id.log_in_Btn);
//
//        String email = mEmail.getText().toString().trim();
//        String pass = mPass.getText().toString().trim();
//        String confPass = mConfPass.getText().toString().trim();
//
//        if(TextUtils.isEmpty(email)){
//            mEmail.setError(("Email Is required"));
//            return;
//        }
//        if(TextUtils.isEmpty(pass)){
//            mPass.setError(("Password Is required"));
//            return;
//        }
//        if(TextUtils.isEmpty(confPass)){
//            mConfPass.setError(("Password Confirmation Is required"));
//            return;
//        }
//        if(!pass.equals(confPass)){
//            mPass.setError(("Passwords are not the same"));
//            return;
//        }
//        if(pass.length() < 6){
//            mPass.setError(("Password Should be at least 6 characters long"));
//            return;
//        }
////        Fragment fragment = new YourFragment();
////
////        FragmentManager fm = getSupportFragmentManager();
////        FragmentTransaction transaction = fm.beginTransaction();
////        transaction.replace(R.id.contentFragment, fragment);
////        transaction.commit();
//
//        mAuth.createUserWithEmailAndPassword(email, pass)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {// אם מצליח
//                            Toast.makeText(view.getContext(), "user created", Toast.LENGTH_LONG).show();// הודעה
////                            startActivity(new Intent(RegisterFragment.this, HomeFragment));// עוברים מסך
//                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
//
//                        }
//                        else {// אם לא מצליח
//                            Toast.makeText(view.getContext(), "failed", Toast.LENGTH_LONG).show();// הודעה
//                        }
//                    }
//                });
//    }

    public void btnLogin(View view){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new LoginFragment<>()).commit();
    }


    public boolean onClick(View v) {
        return false;
    }
}
