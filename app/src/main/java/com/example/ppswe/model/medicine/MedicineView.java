package com.example.ppswe.model.medicine;

import android.os.Parcel;
import android.os.Parcelable;

public class MedicineView implements Parcelable {
    private String medName;
    private int medTime;
    private String medInstruction;
    private int medDose;
    private String medType;
    private String medID;
    private String medStatus;

    public MedicineView() {
    }

    public MedicineView(String medID, String medName, String medInstruction, int medDose, String medType) {
        this.medID = medID;
        this.medName = medName;
        this.medInstruction = medInstruction;
        this.medDose = medDose;
        this.medType = medType;
    }

    protected MedicineView(Parcel in) {
        medName = in.readString();
        medTime = in.readInt();
        medInstruction = in.readString();
        medDose = in.readInt();
        medType = in.readString();
        medID = in.readString();
        medStatus = in.readString();
    }

    public static final Creator<MedicineView> CREATOR = new Creator<MedicineView>() {
        @Override
        public MedicineView createFromParcel(Parcel in) {
            return new MedicineView(in);
        }

        @Override
        public MedicineView[] newArray(int size) {
            return new MedicineView[size];
        }
    };

    public String getMedID() {
        return medID + "." + medTime;
    }

    public void setMedID(String medID) {
        this.medID = medID;
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

    public String getMedStatus() {
        return medStatus;
    }

    public void setMedStatus(String medStatus) {
        this.medStatus = medStatus;
    }

    public String getAllDescription() {
        return "Take " + medDose + " " + medType + " " + medInstruction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(medName);
        parcel.writeInt(medTime);
        parcel.writeString(medInstruction);
        parcel.writeInt(medDose);
        parcel.writeString(medType);
        parcel.writeString(medID);
        parcel.writeString(medStatus);
    }
}
