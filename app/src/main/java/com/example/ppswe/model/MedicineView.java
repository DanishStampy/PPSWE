package com.example.ppswe.model;

public class MedicineView {
    private String medName;
    private int medTime;
    private String medInstruction;
    private int medDose;
    private String medType;

    public MedicineView() {
    }

    public MedicineView(String medName, String medInstruction, int medDose, String medType) {
        this.medName = medName;
        this.medInstruction = medInstruction;
        this.medDose = medDose;
        this.medType = medType;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public int getMedTime() {
        return medTime;
    }

    public void setMedTime(int medTime) {
        this.medTime = medTime;
    }

    public String getMedInstruction() {
        return medInstruction;
    }

    public void setMedInstruction(String medDescription) {
        this.medInstruction = medDescription;
    }

    public int getMedDose() {
        return medDose;
    }

    public void setMedDose(int medDose) {
        this.medDose = medDose;
    }

    public String getMedType() {
        return medType;
    }

    public void setMedType(String medType) {
        this.medType = medType;
    }

    public String getAllDescription() {
        return "Take " + medDose + " " + medType + " " + medInstruction;
    }
}
