package com.example.silent_ver_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.silent_ver_1.databinding.FragmentRegisterBinding;
import com.example.silent_ver_1.ui.user.UserModel;
import com.example.silent_ver_1.ui.user.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mEmail, mPass, mConfPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(MainActivity.this, NavDrawer.class));
            new UserHolder().updateUser(); // updates the user from the the firebase
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void btnRegister(View view) {
        mEmail = findViewById(R.id.email);
        mPass = findViewById(R.id.password);
        mConfPass = findViewById(R.id.password2);

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

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {// אם מצליח
                            UserModel userModel = new UserModel(email);
                            Toast.makeText(MainActivity.this, "user created", Toast.LENGTH_LONG).show();// הודעה
                            new UserHolder().updateUser(); // updates the user from the the firebase
                            startActivity(new Intent(MainActivity.this, NavDrawer.class));// עוברים מסך
                        }
                        else {// אם לא מצליח
                            Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_LONG).show();// הודעה
                        }
                    }
                });
    }

    public void btnLogin(View view){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));// עוברים מסך
    }

}