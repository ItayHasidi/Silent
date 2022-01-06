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
        getFilterList();

    }

    /**
     *  Getting a list of filters of the current user from the Database
     *  Also updating the recycle view to show the filters on screen
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
                // Update the Recycle View
                FilterEditMessage.this.updateRe();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FilterEditMessage.this.updateRe();
    }

    /**
     * Updates the recycle view
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

    public void onSaveClickFilter(View view) {
        String s = msg.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference(currUser+"/Filters").push();
        DatabaseReference ref = database.getReference(currUser).child("Filters");
        DatabaseReference ref2 = database.getReference(currUser).child("Events");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot temp: snapshot.getChildren()){
                    FiltertModel f = temp.getValue(FiltertModel.class);
                    if(f.getFilter().equals(s)){
                        return;
                    }
                }
                FiltertModel temp = new FiltertModel(s);
                myRef.setValue(temp);
                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
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

    public void onClickDel(View view) {
        String s = msg.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://silent-android-application-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference(currUser);
        DatabaseReference myRef2 = database.getReference(currUser).child("Events");
        for(FiltertModel f : arrayList){
            if(f.getFilter().equals(s)){
                arrayList.remove(f);
                Query query = myRef.child("/Filters").orderByChild("filter").equalTo(s);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap: dataSnapshot.getChildren()) {
                            snap.getRef().removeValue();

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