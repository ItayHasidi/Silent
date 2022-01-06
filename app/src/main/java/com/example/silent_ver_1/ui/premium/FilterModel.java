package com.example.silent_ver_1.ui.premium;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Holds data for a filter.
 * A filter is used to filter out only events the user wants to set to Silent.
 */
public class FilterModel {

    private String filterField;

    public FilterModel(){}

    public FilterModel(String t){
        this.filterField =t;
    }

    public String getFilter(){
        return this.filterField;
    }

    public void setFilter(String filter){
        this.filterField = filter;
    }
}
