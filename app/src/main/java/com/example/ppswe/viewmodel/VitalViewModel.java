package com.example.ppswe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ppswe.model.vitalsign.VitalSign;
import com.example.ppswe.repo.VitalSignRepository;

import java.util.ArrayList;
import java.util.List;

public class VitalViewModel extends AndroidViewModel {

    private VitalSignRepository repository;
    private MutableLiveData<ArrayList<VitalSign>> vitalSignData;

    public VitalViewModel(@NonNull Application application) {
        super(application);

        repository = new VitalSignRepository(application);
        vitalSignData = repository.getVitalSignMutableLiveData();
    }

    public void writeVitalSign(VitalSign vitalSign) {
        repository.writeVitalSign(vitalSign);
    }

    public MutableLiveData<ArrayList<VitalSign>> getVitalSignData() {
        return vitalSignData;
    }
}
