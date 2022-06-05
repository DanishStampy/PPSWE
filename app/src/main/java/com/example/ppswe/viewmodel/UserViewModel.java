package com.example.ppswe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ppswe.model.user.User;
import com.example.ppswe.repo.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;
    private MutableLiveData<List<User>> userMutableLiveData;

    public UserViewModel(@NonNull Application application) {
        super(application);

        repository = new UserRepository(application);
        userMutableLiveData = repository.getUserMutableLiveData();
    }

    public MutableLiveData<List<User>> getUserMutableLiveData() {
        return userMutableLiveData;
    }
}
