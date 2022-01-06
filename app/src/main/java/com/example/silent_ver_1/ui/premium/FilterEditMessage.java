package com.example.silent_ver_1.ui.premium;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.silent_ver_1.CalendarAssets.CalendarEventModel;
import com.example.silent_ver_1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FilterEditMessage extends AppCompatActivity {
    String currUser;
    EditText msg;
    private RecyclerView recyclerView;
    private Button saveBtn;
    private Button delBtn;
    private int curPosition = -1;
    public static ArrayList<FiltertModel> arrayList = new ArrayList<>();
    private FilterAdapter adapter;
    public static final String TAG = "ContentValues";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        setContentView(R.layout.activity_filter_edit_message);
        msg = findViewById(R.id.editFilterTwo);
        recyclerView = findViewById(R.id.recyclerViewFilter);
        saveBtn = findViewById(R.id.saveBtnFilter);
        delBtn = findViewById(R.id.deleteBtn);
        // Get the filterList from the database and adding it to the arraylist
        getFilterList();

    }

    /**
     *  Getting a list of filters of the current user from the Database
     *  Also updating the recycler view to show the filters on screen
     */
    private void getFilterList() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/");
        // Getting a reference to the filters that the user set in the past in the Database
        DatabaseReference myRef = database.getReference(currUser+"/Filters");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the current arraylist of filters and adding to it all the filters that written in the Database
                arrayList.clear();
                for(DataSnapshot d : snapshot.getChildren()){
                    if(d.exists()){
                        FiltertModel t = d.getValue(FiltertModel.class);
                        FilterEditMessage.arrayList.add(t);
                    }
                }
                // Update the Recycler View
                FilterEditMessage.this.updateRe();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FilterEditMessage.this.updateRe();
    }

    /**
     * Updates the recycler view with the updated filter list
     */
    public void updateRe(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FilterAdapter(this, FilterEditMessage.arrayList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new FilterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                msg.setText(arrayList.get(position).getFilter());
                curPosition = position;
            }
        });
    }

    /**
     * If the user pressed the save button to save a certain filter then this function check if the filter
     * Is already exist in the database, if it exist the there is no need to save it as a new filter
     * Otherwise we add the filter to the Database
     * Also this function set each event that contains the filter in it's title to silent mode
     * @param view
     */
    public void onSaveClickFilter(View view) {
        String s = msg.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference(currUser+"/Filters").push();
        DatabaseReference ref = database.getReference(currUser).child("Filters");
        DatabaseReference ref2 = database.getReference(currUser).child("Events");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // For each filter in the Database of the current user we check if the filter is exist
                for(DataSnapshot temp: snapshot.getChildren()){
                    FiltertModel f = temp.getValue(FiltertModel.class);
                    if(f.getFilter().equals(s)){
                        return;
                    }
                }
                // If the filter is not exist then create a new FilterModel and save it to the database
                FiltertModel temp = new FiltertModel(s);
                myRef.setValue(temp);
                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        /* For each event in the Database check if the title contains the Filter
                         If it contains set the event to silent Mode */
                        for(DataSnapshot snap : snapshot.getChildren()){
                            CalendarEventModel temp = snap.getValue(CalendarEventModel.class);
                            if(temp.getTitle().contains(s)){
                                snap.child("toMute").getRef().setValue(true);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /**
     * If the user wish to delete a certain filter then:
     * This function checks if the filter exist in the Database under the user branch
     * If it exists then we remove the filter from the Database
     * Each event that contains the filter in the title will be changed to unMute mode
     * @param view
     */
    public void onClickDel(View view) {
        String s = msg.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference(currUser);
        DatabaseReference myRef2 = database.getReference(currUser).child("Events");
        for(FiltertModel f : arrayList){
            if(f.getFilter().equals(s)){
                // Removing the filter from the arraylist
                arrayList.remove(f);
                // Removing the filter from the Database
                Query query = myRef.child("/Filters").orderByChild("filter").equalTo(s);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap: dataSnapshot.getChildren()) {
                            snap.getRef().removeValue();

                            // For each event in the Database that contains filter in the title will be set to unMute mode
                            myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                        CalendarEventModel temp = snapshot1.getValue(CalendarEventModel.class);
                                        if(temp.getTitle().contains(s)){
                                            snapshot1.child("toMute").getRef().setValue(false);
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
                break;
            }
        }
    }
}