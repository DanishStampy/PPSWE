package com.example.ppswe.model.vitalsign;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class VitalSign implements Parcelable {

    private double height;
    private double weight;
    private List<Double> BPrate;
    private double pulseRate;
    private double respirationRate;
    private double bodyTemperature;
    private double BMI;
    private String date;

    public VitalSign() {
    }

    public VitalSign(double height, double weight, List<Double> BPrate, double pulseRate, double respirationRate, double bodyTemperature) {
        this.height = height;
        this.weight = weight;
        this.BPrate = BPrate;
        this.pulseRate = pulseRate;
        this.respirationRate = respirationRate;
        this.bodyTemperature = bodyTemperature;
    }

    protected VitalSign(Parcel in) {
        height = in.readDouble();
        weight = in.readDouble();
        pulseRate = in.readDouble();
        respirationRate = in.readDouble();
        bodyTemperature = in.readDouble();
    }

    public static final Creator<VitalSign> CREATOR = new Creator<VitalSign>() {
        @Override
        public VitalSign createFromParcel(Parcel in) {
            return new VitalSign(in);
        }

        @Override
        public VitalSign[] newArray(int size) {
            return new VitalSign[size];
        }
    };

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public List<Double> getBPrate() {
        return BPrate;
    }

    public void setBPrate(List<Double> BPrate) {
        this.BPrate = BPrate;
    }

    public double getPulseRate() {
        return pulseRate;
    }

    public void setPulseRate(double pulseRate) {
        this.pulseRate = pulseRate;
    }

    public double getRespirationRate() {
        return respirationRate;
    }

    public void setRespirationRate(double respirationRate) {
        this.respirationRate = respirationRate;
    }

    public double getBodyTemperature() {
        return bodyTemperature;
    }

    public void setBodyTemperature(double bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }

    public void setBMI(double BMI) { this.BMI = BMI;}

    public double getBMI() {
        return BMI;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(height);
        parcel.writeDouble(weight);
        parcel.writeDouble(pulseRate);
        parcel.writeDouble(respirationRate);
        parcel.writeDouble(bodyTemperature);
    }
}
