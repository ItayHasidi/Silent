package com.example.silent_ver_1.ui.user;

import androidx.annotation.NonNull;

import com.example.silent_ver_1.CalendarAssets.CalendarEventModel;
import com.example.silent_ver_1.ui.premium.ContactModel;
import com.example.silent_ver_1.ui.premium.FilterModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserModel {
    private String email;
    private boolean isPremium, isSilent;
    private String currUser;
    private ArrayList<ContactModel> contacts;
    private ArrayList<CalendarEventModel> events;
    private DatabaseReference myRef;
    private ArrayList<FilterModel> filters;



    public UserModel(){
        contacts = new ArrayList<>();
        events = new ArrayList<>();
        filters = new ArrayList<>();
    }

    /**
     * Constructor who also updates Database
     * @param email
     */
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

    /**
     * Set email and updating the Database
     * @param email
     */
    public void setEmail(String email) { this.email = email; updateFirebase("email", email); }

    public boolean isPremium() {
        return isPremium;
    }

    /**
     * Set the premium status and updating the Database
     * @param premium
     */
    public void setPremium(boolean premium) { isPremium = premium; updateFirebase("premium", isPremium); }

    public boolean isSilent() { return isSilent;}

    /**
     * Set the current mode of phone and updating the Database
     * @param silent
     */
    public void setSilent(boolean silent) { isSilent = silent; updateFirebase("silent", isSilent);}

    public ArrayList<ContactModel> getContacts() {
        return contacts;
    }

    /**
     * Set a list of ContactModel and updates the Database
     * @param contacts
     */
    public void setContacts(ArrayList<ContactModel> contacts) {
        this.contacts = new ArrayList<>(contacts);
        for(ContactModel tempCon: contacts){
            updateFirebase("Contacts/"+tempCon.getNumber(), tempCon.getMessage());
        }
    }

    public ArrayList<CalendarEventModel> getEvents() {
        return events;
    }


    /**
     * Set a list of event to the current user and in some specific cases updating the Firebase database with
     * this list.
     * Also if the function updates the database then we check if each event is already set to mute in the database
     * @param events
     * @param isToUpdateFirebase
     */
    public void setEvents(ArrayList<CalendarEventModel> events, boolean isToUpdateFirebase) {
        if(currUser == null){
            currUser =FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        this.events = new ArrayList<>(events);
        if(true /*isToUpdateFirebase*/) {
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
                        }
                        updateFirebase("Events/" + event.getId(), event);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    /**
     * Update the status of "toMute" in the Database for specific event
     * @param event
     * @param isToUpdateFirebase
     */
    public void setMuteEvent(CalendarEventModel event, boolean isToUpdateFirebase) {
        if(isToUpdateFirebase) {
            updateFirebase("Events/" + event.getId()+"/toMute/", event.isToMute());
        }
    }

    /**
     * Updates the Database with the user's information
     */
    private void updateFirebase(){
        this.currUser = FirebaseAuth.getInstance().getUid();
        myRef = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference(currUser+"");
        myRef.setValue(this);
    }

    /**
     * Updates the Database in the branch that represented by "cmd" and set it's value to "val"
     * @param cmd
     * @param val
     */
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

    /**
     * Set a list of FilterModel
     * @param tempArr
     */
    public void setFilters(ArrayList<FilterModel> tempArr) {
        this.filters = new ArrayList<>(tempArr);
    }

    /**
     * Get a list of filters of the current user
     * @return
     */
    public ArrayList<FilterModel> getFilters(){
        return this.filters;
    }
}
