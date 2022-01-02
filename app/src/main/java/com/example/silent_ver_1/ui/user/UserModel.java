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
import java.util.Arrays;

public class UserModel {
    private String email;
    private boolean isPremium, isSilent;
    private String currUser;
    private ArrayList<ContactModel> contacts;
    private ArrayList<CalendarEventModel> events;
    private DatabaseReference myRef;

    private boolean isWait = true;

    public UserModel(){
        //getUser();
        contacts = new ArrayList<>();
        events = new ArrayList<>();
    }

    public UserModel(String email) {
        this.email = email;
        this.isPremium = true;
        this.isSilent = false;
        this.contacts = new ArrayList<>();
        this.events = new ArrayList<>();
        updateFirebase();
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) { this.email = email; updateFirebase("email", email); }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) { isPremium = premium; updateFirebase("premium", isPremium); }

    public boolean isSilent() { return isSilent;}

    public void setSilent(boolean silent) { isSilent = silent; updateFirebase("silent", isSilent);}

    public ArrayList<ContactModel> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<ContactModel> contacts) {
        this.contacts = new ArrayList<>(contacts);
        for(ContactModel tempCon: contacts){
            updateFirebase("Contacts/"+tempCon.getNumber(), tempCon.getMessage());
        }
    }

    public ArrayList<CalendarEventModel> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<CalendarEventModel> events, boolean isToUpdateFirebase) {
        this.events = new ArrayList<>(events);
        if(isToUpdateFirebase) {
            for (CalendarEventModel event : events) {
                updateFirebase("Events/" + event.getId(), event);
            }
        }
    }

    public void setMuteEvent(CalendarEventModel event, boolean isToUpdateFirebase) {
        if(isToUpdateFirebase) {
            updateFirebase("Events/" + event.getId()+"/toMute/", event.isToMute());
        }
    }

    private void updateFirebase(){
        this.currUser = FirebaseAuth.getInstance().getUid();
        myRef = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference(currUser+"");
        myRef.setValue(this);
    }

    private void updateFirebase(String cmd, Object val){
        this.currUser = FirebaseAuth.getInstance().getUid();
        myRef = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference(currUser+"/"+cmd);
        myRef.setValue(val);
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "Email='" + email + '\'' +
                ", isPremium=" + isPremium +
                ", isSilent=" + isSilent +
                ", Contacts=" + contacts +
                ", Events=" + events +
                '}';
    }
}
