package com.example.ppswe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ppswe.repo.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {

    private AuthRepository repository;
    private MutableLiveData<FirebaseUser> userData;
    private MutableLiveData<Boolean> loggedStatus;

    public AuthViewModel(@NonNull Application application) {
        super(application);

        repository = new AuthRepository(application);
        userData = repository.getFirebaseUserMutableLiveData();
        loggedStatus = repository.getUserLoggedMutableLiveData();

    }

    public MutableLiveData<FirebaseUser> getUserData() {
        return userData;
    }

    public MutableLiveData<Boolean> getLoggedStatus() {
        return loggedStatus;
    }


    public void register(String username, String email, String password, String phoneNum, String roles){
        repository.register(username, email, password, phoneNum, roles);
    }

    public void login(String email, String password){
        repository.login(email, password);
    }

    public void signOut(){
        repository.signOut();
    }
}
