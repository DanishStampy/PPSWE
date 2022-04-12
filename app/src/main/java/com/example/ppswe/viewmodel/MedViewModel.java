package com.example.ppswe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ppswe.repo.MedRepository;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MedViewModel extends AndroidViewModel {

    private MedRepository repository;
    private MutableLiveData<FirebaseUser> userData;

    public MedViewModel(@NonNull Application application) {
        super(application);

        repository = new MedRepository(application);
        userData = repository.getFirebaseUserMutableLiveData();
    }

    public void writeMed(String medName, String medType, int medDose, int medFreq, List<String> medTimes, String medInstruction, String medDesc){
        repository.writeMed(medName, medType, medDose, medFreq, medTimes, medInstruction, medDesc);
    }
}
