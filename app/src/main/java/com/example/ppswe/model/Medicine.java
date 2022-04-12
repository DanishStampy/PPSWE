package com.example.ppswe.model;

import java.util.List;

public class Medicine {

    private static String medName;
    private static String medType;
    private static int medDose;
    private static int medFreq;
    private static List<String> medTimes;
    private static String medInstruction;
    private static String medDesc;

    public Medicine() {
    }

    public Medicine(String medName, String medType, int medDose, int medFreq, List<String> medTimes, String medInstruction, String medDesc) {
        this.medName = medName;
        this.medType = medType;
        this.medDose = medDose;
        this.medFreq = medFreq;
        this.medTimes = medTimes;
        this.medInstruction = medInstruction;
        this.medDesc = medDesc;
    }

    public static String getMedName() {
        return medName;
    }

    public static String getMedType() {
        return medType;
    }

    public static int getMedDose() {
        return medDose;
    }

    public static int getMedFreq() {
        return medFreq;
    }

    public static List<String> getMedTimes() {
        return medTimes;
    }

    public static String getMedInstruction() {
        return medInstruction;
    }

    public static String getMedDesc() {
        return medDesc;
    }

    public static void setMedName(String medName) {
        Medicine.medName = medName;
    }

    public static void setMedType(String medType) {
        Medicine.medType = medType;
    }

    public static void setMedDose(int medDose) {
        Medicine.medDose = medDose;
    }

    public static void setMedFreq(int medFreq) {
        Medicine.medFreq = medFreq;
    }

    public static void setMedTimes(List<String> medTimes) {
        Medicine.medTimes = medTimes;
    }

    public static void setMedInstruction(String medInstruction) {
        Medicine.medInstruction = medInstruction;
    }

    public static void setMedDesc(String medDesc) {
        Medicine.medDesc = medDesc;
    }
}
