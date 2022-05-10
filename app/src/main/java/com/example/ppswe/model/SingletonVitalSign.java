package com.example.ppswe.model;

import java.util.List;

public class SingletonVitalSign {
    double height;
    double weight;
    List<Double> BP;
    double bodyTemp;
    double pulseRate;
    double respiraitonRate;

    private static final SingletonVitalSign ourInstance = new SingletonVitalSign();
    public static SingletonVitalSign getInstance() { return ourInstance; }

    public SingletonVitalSign() {
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

    public double getRespiraitonRate() {
        return respiraitonRate;
    }

    public void setRespiraitonRate(double respiraitonRate) {
        this.respiraitonRate = respiraitonRate;
    }
}
