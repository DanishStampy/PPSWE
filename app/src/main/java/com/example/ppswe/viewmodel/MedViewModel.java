package com.example.ppswe.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ppswe.model.medicine.Medicine;
import com.example.ppswe.model.medicine.MedicineStatus;
import com.example.ppswe.model.medicine.MedicineView;
import com.example.ppswe.model.report.ReportFile;
import com.example.ppswe.repo.MedRepository;

import java.util.ArrayList;
import java.util.List;

public class MedViewModel extends AndroidViewModel {

    private MedRepository repository;
    private MutableLiveData<ArrayList<MedicineView>> medData;
    private MutableLiveData<ArrayList<MedicineView>> medDataCaregiver;
    private MutableLiveData<ArrayList<Integer>> statusCountList;
    private MutableLiveData<ReportFile> reportData;
    private MutableLiveData<ArrayList<Medicine>> medListData;

    public MedViewModel(@NonNull Application application) {
        super(application);

        repository = new MedRepository(application);
        medData = repository.getMedicineArrayList();
        statusCountList = repository.getReportStatusCountList();
        reportData = repository.getReportDetail();
        medListData = repository.getMedicineDataArrayList();

        // Caregiver
        medDataCaregiver = repository.getMedicineArrayListCaregiver();
    }

    public void deleteMedData(String medId) {
        repository.deleteMedData(medId);
    }

    public void deleteMedTime(String medId, int medTime) {
        repository.deleteMedTime(medId, medTime);
    }

    public void updateMed(Medicine medicine) {
        repository.updateMed(medicine);
    }

    public void writeMed(Medicine medicine){
        repository.writeMed(medicine);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateMedStatus(MedicineStatus status) {
        repository.updateMedStatus(status);
    }

    public MutableLiveData<ArrayList<Medicine>> getMedListData() {
        return medListData;
    }

    public MutableLiveData<ArrayList<MedicineView>> getMedData() {
        return medData;
    }

    public MutableLiveData<ArrayList<Integer>> getStatusCountList() {
        return statusCountList;
    }

    public MutableLiveData<ReportFile> getReportData() {
        return reportData;
    }

    public MutableLiveData<ArrayList<MedicineView>> getMedDataCaregiver() {
        return medDataCaregiver;
    }
}
