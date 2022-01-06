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

/**
 * The first page that is loaded and the page where the user can register to the app.
 */
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mEmail, mPass, mConfPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Checks if there is a user logged on, in case there is the function loads the NavDrawer page and updates the user from the DB.
     */
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

    /**
     * Checks for permissions.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Processes all the data given from the user and creates a new user in Firebase.
     * @param view
     */
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

        /**
         * This is where the user is created in the Firebase.
         */
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

    /**
     * In case the user has already an account, the user can be directed to the Login page.
     * @param view
     */
    public void btnLogin(View view){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));// עוברים מסך
    }
}