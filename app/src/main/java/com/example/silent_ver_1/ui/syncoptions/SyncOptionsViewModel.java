package com.example.silent_ver_1.ui.syncoptions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SyncOptionsViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public SyncOptionsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Sync options fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
