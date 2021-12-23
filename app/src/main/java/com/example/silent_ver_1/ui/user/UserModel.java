package com.example.silent_ver_1.ui.user;

import static android.content.ContentValues.TAG;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.silent_ver_1.CalendarAssets.CalendarEventModel;
import com.example.silent_ver_1.ui.premium.ContactModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserModel {
    private String email;
    private boolean isPremium;
    private String currUser;
    private ArrayList<ContactModel> contacts;
    private ArrayList<CalendarEventModel> events;
    private DatabaseReference myRef;

    public UserModel(){

    }

    public UserModel(String email) {
        this.email = email;
        this.isPremium = true;
        this.contacts = new ArrayList<>();
        this.events = new ArrayList<>();
        this.currUser = FirebaseAuth.getInstance().getUid();
        myRef = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference(currUser+"");
        myRef.setValue(this);
    }

    public void getUser(){
        this.currUser = FirebaseAuth.getInstance().getUid();
        myRef = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/").getReference(currUser);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot tempData: snapshot.getChildren()){

                    if(tempData.getKey().equals("email")){
                        UserModel.this.email = tempData.getValue(String.class);
//                        Log.i(TAG, "user model email: "+tempData.getValue(String.class) + " , "+ UserModel.this.email);
                    }
                    else if(tempData.getKey().equals("premium")){
                        UserModel.this.isPremium = tempData.getValue(Boolean.class);
//                        Log.i(TAG, "user model premium: "+tempData.getValue(Boolean.class) + " , "+ UserModel.this.isPremium);
                    }
                    else if(tempData.getKey().equals("contacts")){
                        UserModel.this.contacts = tempData.getValue(ArrayList.class);
//                        Log.i(TAG, "user model contacts: "+tempData.getValue(ArrayList.class) + " , "+ UserModel.this.contacts);
                    }
                    else if(tempData.getKey().equals("events")){
                        UserModel.this.events = tempData.getValue(ArrayList.class);
//                        Log.i(TAG, "user model events: "+tempData.getValue(ArrayList.class) + " , "+ UserModel.this.events);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.i(TAG, "user model email2: ");
            }
        });

//        Log.i(TAG, "user model email1: "+this.email);
//        Log.i(TAG, "user model premium1: "+this.isPremium);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public ArrayList<ContactModel> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<ContactModel> contacts) {
        this.contacts = contacts;
    }

    public ArrayList<CalendarEventModel> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<CalendarEventModel> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "email='" + email + '\'' +
                ", isPremium=" + isPremium +
                ", contacts=" + contacts +
                ", events=" + events +
                '}';
    }
}
