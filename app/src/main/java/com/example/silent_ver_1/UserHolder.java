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

    public UserHolder(){
        user = new UserModel();
        updateUser();
    }

    public static UserModel getUser() {
        if(firstFlag){

            firstFlag = false;
//            new UserHolder();
            Log.i(TAG, "UserHolder: "+user);
        }
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public void updateUser(){

        this.currUser = FirebaseAuth.getInstance().getUid();
        Log.i(TAG, "in here "+ currUser);
        myRef = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/").getReference(currUser);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for(DataSnapshot tempData: snapshot.getChildren()){
                    Log.i(TAG, "in here ");
                    if(tempData.getKey().equals("email")){
                        UserHolder.this.user.setEmail(tempData.getValue(String.class));
//                        Log.i(TAG, "user model email: "+tempData.getValue(String.class) /*+ " , "+ UserModel.this.email*/);
                    }
                    else if(tempData.getKey().equals("premium")){
                        UserHolder.this.user.setPremium(tempData.getValue(Boolean.class));
//                        Log.i(TAG, "user model premium: "+tempData.getValue(Boolean.class) + " , "+ UserModel.this.isPremium);
                    }
                    else if(tempData.getKey().equals("silent")){
                        UserHolder.this.user.setSilent(tempData.getValue(Boolean.class));
//                        Log.i(TAG, "user model premium: "+tempData.getValue(Boolean.class) + " , "+ UserModel.this.isPremium);
                    }
                    else if(tempData.getKey().equals("Contacts")){
                        ArrayList<ContactModel> tempArr = new ArrayList<>();
                        for(DataSnapshot tempCon: tempData.getChildren()){
//                            Log.i(TAG, "UserModel: "+tempCon.getValue(String.class).toString() + " , "+tempCon.getKey());
                            tempArr.add(new ContactModel(tempCon.getKey(), tempCon.getValue(String.class)));
//                            tempArr.add(tempCon.getValue(ContactModel.class));
                        }
                        UserHolder.this.user.setContacts(tempArr);
//                        Log.i(TAG, "UserModel: in contacts ");
////                        UserModel.this.contacts =
//                        ArrayList<ContactModel> tempArr =  tempData.getValue(ArrayList.class);
//                        Log.i(TAG, "UserModel: contacts: "+tempData.getValue(ArrayList.class) + " , "+ UserModel.this.contacts);
                    }
                    else if(tempData.getKey().equals("Events")){

                        ArrayList<CalendarEventModel> tempArr = new ArrayList<>();
                        for(DataSnapshot tempEvent: tempData.getChildren()){
                            tempArr.add(tempEvent.getValue(CalendarEventModel.class));
                        }
                        UserHolder.this.user.setEvents(tempArr, false);
//                        Log.i(TAG, "UserModel: in events "+ events);

//                        Log.i(TAG, "UserModel: in events");
//                        UserModel.this.events = tempData.getValue(ArrayList.class);
//                        Log.i(TAG, "UserModel: events: "+tempData.getValue(ArrayList.class) + " , "+ UserModel.this.events);
                    }
                    else if(tempData.getKey().equals("Filters")){
                        ArrayList<FiltertModel> tempArr = new ArrayList<>();
                        for(DataSnapshot tempFilter : tempData.getChildren()){
                            tempArr.add(tempFilter.getValue(FiltertModel.class));
                        }
                        UserHolder.this.user.setFilters(tempArr);
                    }
//                    else if(tempData.getKey().equals("HasSyncAlarm")){
//                        Log.i(TAG, "try again checking alarm"+ tempData.getValue(Boolean.class));
//                        UserHolder.this.user.setHasSyncAlarm(tempData.getValue(Boolean.class));
//                    }

                }
                //Log.i(TAG, "UserModel: "+UserModel.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.i(TAG, "user model email2: ");
            }
        });

//        Log.i(TAG, "user model email1: "+this.email);
//        Log.i(TAG, "user model premium1: "+this.isPremium);
        //while(isWait){}
    }

//    public  boolean checkFilterExist(FiltertModel filtertModel){
//        boolean ans = false;
//        FirebaseDatabase database = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/");
//        DatabaseReference myRef = database.getReference(currUser);
//        Query query = myRef.child("/adc").orderByChild("filter").equalTo(filtertModel.getFilter());
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
//                    ans = false;
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e(TAG, "onCancelled", databaseError.toException());
//            }
//        });
//        return ans;
//    }
}
