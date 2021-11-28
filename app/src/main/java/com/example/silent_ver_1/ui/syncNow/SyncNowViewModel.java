package com.example.silent_ver_1.ui.syncNow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SyncNowViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public SyncNowViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Sync now fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
