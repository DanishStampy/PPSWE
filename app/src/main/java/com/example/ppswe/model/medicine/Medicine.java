package com.example.ppswe.model.medicine;

import java.util.List;

public class Medicine {

    private String medName;
    private String medType;
    private int medDose;
    private int medFreq;
    private List<Integer> medTimes;
    private String medInstruction;
    private String medDesc;

    public Medicine() {
    }

    public Medicine(String medName, String medType, int medDose, int medFreq, List<Integer> medTimes, String medInstruction, String medDesc) {
        this.medName = medName;
        this.medType = medType;
        this.medDose = medDose;
        this.medFreq = medFreq;
        this.medTimes = medTimes;
        this.medInstruction = medInstruction;
        this.medDesc = medDesc;
    }

    public String getMedName() {
        return medName;
    }

    public String getMedType() {
        return medType;
    }

    public int getMedDose() {
        return medDose;
    }

    public int getMedFreq() {
        return medFreq;
    }

    public List<Integer> getMedTimes() {
        return medTimes;
    }

    public String getMedInstruction() {
        return medInstruction;
    }

    public String getMedDesc() {
        return medDesc;
    }
}
