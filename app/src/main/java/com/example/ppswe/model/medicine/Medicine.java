package com.example.ppswe.model.medicine;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Medicine implements Parcelable {

    private String medName;
    private String medType;
    private int medDose;
    private int medFreq;
    private List<Integer> medTimes;
    private String medInstruction;
    private String medDesc;
    private String medId;

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

//    Parcelable method

    protected Medicine(Parcel in) {
        medName = in.readString();
        medType = in.readString();
        medDose = in.readInt();
        medFreq = in.readInt();
        medInstruction = in.readString();
        medDesc = in.readString();
        medId = in.readString();
    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(medName);
        parcel.writeString(medType);
        parcel.writeInt(medDose);
        parcel.writeInt(medFreq);
        parcel.writeString(medInstruction);
        parcel.writeString(medDesc);
        parcel.writeString(medId);
    }

    public String getMedId() {
        return medId;
    }

    public void setMedId(String medId) {
        this.medId = medId;
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

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public void setMedType(String medType) {
        this.medType = medType;
    }

    public void setMedDose(int medDose) {
        this.medDose = medDose;
    }

    public void setMedFreq(int medFreq) {
        this.medFreq = medFreq;
    }

    public void setMedTimes(List<Integer> medTimes) {
        this.medTimes = medTimes;
    }

    public void setMedInstruction(String medInstruction) {
        this.medInstruction = medInstruction;
    }

    public void setMedDesc(String medDesc) {
        this.medDesc = medDesc;
    }
}
