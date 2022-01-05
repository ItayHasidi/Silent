package com.example.silent_ver_1.ui.user;

import static android.content.ContentValues.TAG;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.silent_ver_1.CalendarAssets.CalendarEventModel;
import com.example.silent_ver_1.ui.premium.ContactModel;
import com.example.silent_ver_1.ui.premium.FiltertModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Filter;

public class UserModel {
    private String email;
    private boolean isPremium, isSilent;
    private String currUser;
    private ArrayList<ContactModel> contacts;
    private ArrayList<CalendarEventModel> events;
    private DatabaseReference myRef;
    private ArrayList<FiltertModel> filters;
    private boolean hasSyncAlarm;

    private boolean isWait = true;

    public UserModel(){
        //getUser();
        contacts = new ArrayList<>();
        events = new ArrayList<>();
        filters = new ArrayList<>();
    }

    public UserModel(String email) {
        this.email = email;
        this.isPremium = true;
        this.isSilent = false;
        this.contacts = new ArrayList<>();
        this.events = new ArrayList<>();
        this.filters = new ArrayList<>();
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
        if(currUser == null){
            currUser =FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        this.events = new ArrayList<>(events);
        if(true) {
            for (CalendarEventModel event : events) {
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference(currUser);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot type1 : snapshot.getChildren()){
                           if(type1.getKey().equals("Events")){
                               for(DataSnapshot ev : type1.getChildren()){
                                   CalendarEventModel event1 = ev.getValue(CalendarEventModel.class);
                                   if((event1.getId() == event.getId()) && event1.isToMute()){
                                       event.setToMute(true);
                                       break;
                                   }
                               }
                           }
//                            if(isPremium && type1.getKey().equals("Filters")){
//                                for(DataSnapshot filt : type1.getChildren()){
//                                    FiltertModel temp = filt.getValue(FiltertModel.class);
//                                    if(event.getTitle().contains(temp.getFilter())){
//                                        event.setToMute(true);
//                                        break;
//                                    }
//                                }
//                            }

                        }
                        updateFirebase("Events/" + event.getId(), event);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }




//        if(isToUpdateFirebase) {
//            for (CalendarEventModel event : events) {
//                FirebaseDatabase database = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/");
//                DatabaseReference myRef = database.getReference(currUser).child("Filters");
//                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
//                            FiltertModel temp = snapshot1.getValue(FiltertModel.class);
//                            if(isPremium){
//                                if(event.getTitle().contains(temp.getFilter())){
//                                    event.setToMute(true);
//                                    break;
//                                }
//                            }
//                        }
//                        updateFirebase("Events/" + event.getId(), event);
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        }
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

    public void setFilters(ArrayList<FiltertModel> tempArr) {
        this.filters = new ArrayList<>(tempArr);
    }

    public ArrayList<FiltertModel> getFilters(){
        return this.filters;
    }

    public boolean isHasSyncAlarm() {
        return hasSyncAlarm;
    }
    public void setHasSyncAlarm(boolean hasSyncAlarm) { hasSyncAlarm = hasSyncAlarm; updateFirebase("HasSyncAlarm", hasSyncAlarm);}
}
