package com.example.ppswe.model.vitalsign;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class VitalSign {

    private double height;
    private double weight;
    private List<Double> BPrate;
    private double pulseRate;
    private double respirationRate;
    private double bodyTemperature;

    @ServerTimestamp
    private Date date;

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

    public double getBMI() {
        return weight/(height*height);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}