package com.example.ppswe.model.medicine;

public class MedicineStatus {

    private String medId;
    private String date;
    private String medTime;
    private String medStatus;

    public MedicineStatus() {
    }

    public MedicineStatus(String medId, String medTime, String date, String medStatus) {
        this.medId = medId;
        this.date = date;
        this.medTime = medTime;
        this.medStatus = medStatus;
    }

    public String getMedId() {
        return medId;
    }

    public void setMedId(String medId) {
        this.medId = medId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMedTime() {
        return medTime;
    }

    public void setMedTime(String medTime) {
        this.medTime = medTime;
    }

    public String getMedStatus() {
        return medStatus;
    }

    public void setMedStatus(String medStatus) {
        this.medStatus = medStatus;
    }
}
