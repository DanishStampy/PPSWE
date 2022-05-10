package com.example.ppswe.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class VitalSignParcel implements Parcelable {

    private double height;
    private double weight;
    private double BMI;
    private List<Double> BP;
    private double bodyTemp;
    private double pulseRate;
    private double respiratoryRate;

    protected VitalSignParcel(Parcel in) {
        BP = new ArrayList<Double>();

        this.height = in.readDouble();
        this.weight = in.readDouble();
        this.BMI = in.readDouble();
        in.readList(BP, Double.class.getClassLoader());
        this.bodyTemp = in.readDouble();
        this.pulseRate = in.readDouble();
        this.respiratoryRate = in.readDouble();
    }

    public static final Creator<VitalSignParcel> CREATOR = new Creator<VitalSignParcel>() {
        @Override
        public VitalSignParcel createFromParcel(Parcel in) {
            return new VitalSignParcel(in);
        }

        @Override
        public VitalSignParcel[] newArray(int size) {
            return new VitalSignParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.height);
        parcel.writeDouble(this.weight);
        parcel.writeList(this.BP);
        parcel.writeDouble(this.bodyTemp);
        parcel.writeDouble(this.pulseRate);
        parcel.writeDouble(this.respiratoryRate);
    }

    public VitalSignParcel(double height, double weight, double BMI, List<Double> BP, double bodyTemp, double pulseRate, double respiratoryRate) {
        this.height = height;
        this.weight = weight;
        this.BMI = BMI;
        this.BP = BP;
        this.bodyTemp = bodyTemp;
        this.pulseRate = pulseRate;
        this.respiratoryRate = respiratoryRate;
    }

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

    public double getBMI() {
        return BMI;
    }

    public void setBMI(double BMI) {
        this.BMI = BMI;
    }

    public List<Double> getBP() {
        return BP;
    }

    public void setBP(List<Double> BP) {
        this.BP = BP;
    }

    public double getBodyTemp() {
        return bodyTemp;
    }

    public void setBodyTemp(double bodyTemp) {
        this.bodyTemp = bodyTemp;
    }

    public double getPulseRate() {
        return pulseRate;
    }

    public void setPulseRate(double pulseRate) {
        this.pulseRate = pulseRate;
    }

    public double getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(double respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    @Override
    public String toString() {
        return "VitalSignParcel{" +
                "height=" + height +
                ", weight=" + weight +
                ", BMI=" + BMI +
                ", BP=" + BP +
                ", bodyTemp=" + bodyTemp +
                ", pulseRate=" + pulseRate +
                ", respiratoryRate=" + respiratoryRate +
                '}';
    }
}
