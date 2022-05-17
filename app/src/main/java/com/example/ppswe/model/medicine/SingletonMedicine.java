package com.example.ppswe.model.medicine;

import java.util.List;

public class SingletonMedicine {
    String medName;
    String medType;
    int medDose;
    int medFreq;
    List<Integer> medTimes;
    String medInstruction;
    String medDesc;

    private static final SingletonMedicine ourInstance = new SingletonMedicine();
    public static SingletonMedicine getInstance() {
        return ourInstance;
    }

    private SingletonMedicine () {}

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getMedType() {
        return medType;
    }

    public void setMedType(String medType) {
        this.medType = medType;
    }

    public int getMedDose() {
        return medDose;
    }

    public void setMedDose(int medDose) {
        this.medDose = medDose;
    }

    public int getMedFreq() {
        return medFreq;
    }

    public void setMedFreq(int medFreq) {
        this.medFreq = medFreq;
    }

    public List<Integer> getMedTimes() {
        return medTimes;
    }

    public void setMedTimes(List<Integer> medTimes) {
        this.medTimes = medTimes;
    }

    public String getMedInstruction() {
        return medInstruction;
    }

    public void setMedInstruction(String medInstruction) {
        this.medInstruction = medInstruction;
    }

    public String getMedDesc() {
        return medDesc;
    }

    public void setMedDesc(String medDesc) {
        this.medDesc = medDesc;
    }
}
