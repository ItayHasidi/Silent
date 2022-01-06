package com.example.silent_ver_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * In this class the user can log onto the app using an existing account.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mEmail, mPass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Moves the user to the Register page.
     * @param view
     */
    public void btnRegister(View view){
        startActivity(new Intent(LoginActivity.this, MainActivity.class));// עוברים מסך
    }

    /**
     * Validates that the user has given accurate details and logs the user on.
     * @param view
     */
    public void btnLogin(View view){

        mEmail = findViewById(R.id.email);
        mPass = findViewById(R.id.password);

        String email = mEmail.getText().toString().trim();
        String pass = mPass.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            mEmail.setError(("Email Is required"));
            return;
        }
        if(TextUtils.isEmpty(pass)){
            mPass.setError(("Password Is required"));
            return;
        }


        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {// אם מצליח
                            Toast.makeText(LoginActivity.this, "logged in", Toast.LENGTH_LONG).show();// הודעה
                            new UserHolder().updateUser(); // updates the user from the the firebase
                            startActivity(new Intent(LoginActivity.this, NavDrawer.class));// עוברים מסך
                        }
                        else {// אם לא מצליח
                            Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_LONG).show();// הודעה
                        }
                    }
                });

    }
}