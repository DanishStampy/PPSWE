package com.example.ppswe.model;

import java.util.List;

public class VitalSign {

    private double height;
    private double weight;
    private List<Double> BPrate;
    private double pulseRate;
    private double respirationRate;
    private double bodyTemperature;

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
}
