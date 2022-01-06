package com.example.silent_ver_1;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.silent_ver_1.CalendarAssets.CalendarEventModel;
import com.example.silent_ver_1.ui.premium.ContactModel;
import com.example.silent_ver_1.ui.premium.FiltertModel;
import com.example.silent_ver_1.ui.user.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public final class UserHolder {
    private static UserModel user;
    private static String currUser;
    private static DatabaseReference myRef;
    private static boolean firstFlag = true;

    /**
     * A constructor that syncs the user from the DB.
     */
    public UserHolder(){
        user = new UserModel();
        updateUser();
    }

    public static UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    /**
     * Gets all the saved fields under the user from the DB and sets them to thr held user.
     */
    public void updateUser(){

        this.currUser = FirebaseAuth.getInstance().getUid();
        Log.i(TAG, "in here "+ currUser);
        myRef = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/").getReference(currUser);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                /**
                 * Iterates through all the data and sorts out the relevant data to save.
                 */
                for(DataSnapshot tempData: snapshot.getChildren()){
                    Log.i(TAG, "in here ");
                    if(tempData.getKey().equals("email")){
                        UserHolder.this.user.setEmail(tempData.getValue(String.class));
                    }
                    else if(tempData.getKey().equals("premium")){
                        UserHolder.this.user.setPremium(tempData.getValue(Boolean.class));
                    }
                    else if(tempData.getKey().equals("silent")){
                        UserHolder.this.user.setSilent(tempData.getValue(Boolean.class));
                    }
                    else if(tempData.getKey().equals("Contacts")){
                        ArrayList<ContactModel> tempArr = new ArrayList<>();
                        for(DataSnapshot tempCon: tempData.getChildren()){
                            tempArr.add(new ContactModel(tempCon.getKey(), tempCon.getValue(String.class)));
                        }
                        UserHolder.this.user.setContacts(tempArr);
                    }
                    else if(tempData.getKey().equals("Events")){

                        ArrayList<CalendarEventModel> tempArr = new ArrayList<>();
                        for(DataSnapshot tempEvent: tempData.getChildren()){
                            tempArr.add(tempEvent.getValue(CalendarEventModel.class));
                        }
                        UserHolder.this.user.setEvents(tempArr, false);
                    }
                    else if(tempData.getKey().equals("Filters")){
                        ArrayList<FiltertModel> tempArr = new ArrayList<>();
                        for(DataSnapshot tempFilter : tempData.getChildren()){
                            tempArr.add(tempFilter.getValue(FiltertModel.class));
                        }
                        UserHolder.this.user.setFilters(tempArr);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
