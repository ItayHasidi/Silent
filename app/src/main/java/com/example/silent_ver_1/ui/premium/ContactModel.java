package com.example.silent_ver_1.ui.premium;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactModel {
    private String name;
    private String number;
    private String message = "Can't talk, call you back later.";
    private String currUser;

    public ContactModel(){}

    public ContactModel(String number, String message){
        this.number = number;
        this.message = message;
    }

    /**
     * Constructor that also check the Database if the ContactModel have a specific message
     * @param name
     * @param number
     * @param currUser
     */
    public ContactModel(String name, String number, String currUser/*, Context context*/){
        this.name = name;
        this.number = number;
        // retrieve the saved message from the database (Firebase)
        DatabaseReference myRef = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference(currUser+"/Contacts");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot curSnapshot : snapshot.getChildren()) {
                    if (curSnapshot.getKey().toString().equals(number)) {
                        setMessage(curSnapshot.getValue().toString());
                        return;
                    }
                }
                setMessage("Can't talk, call you back later.");
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }});
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
}
