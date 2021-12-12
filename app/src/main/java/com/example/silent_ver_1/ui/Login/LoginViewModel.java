package com.example.silent_ver_1.ui.Login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LoginViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("This is Login fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
