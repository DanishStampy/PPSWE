package com.example.ppswe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ppswe.model.Medicine;
import com.example.ppswe.repo.MedRepository;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MedViewModel extends AndroidViewModel {

    private MedRepository repository;
    private MutableLiveData<ArrayList<Medicine>> medData;

    public MedViewModel(@NonNull Application application) {
        super(application);

        repository = new MedRepository(application);
        medData = repository.getMedicineArrayList();
    }

    public void writeMed(String medName, String medType, int medDose, int medFreq, List<Integer> medTimes, String medInstruction, String medDesc){
        repository.writeMed(medName, medType, medDose, medFreq, medTimes, medInstruction, medDesc);
    }

    public MutableLiveData<ArrayList<Medicine>> getMedData() {
        return medData;
    }
}
