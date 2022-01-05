package com.example.silent_ver_1.ui.premium;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FiltertModel {

    private String filterField;

    public FiltertModel(){}

    public FiltertModel(String t){
        this.filterField =t;
    }


    public String getFilter(){
        return this.filterField;
    }

    public void setFilter(String filter){
        this.filterField = filter;
    }




}
