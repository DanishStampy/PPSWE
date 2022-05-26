package com.example.ppswe.model.user;

public class SingletonStatusPatient {
    private String patientEmail;
    private String patientName;

    public static final SingletonStatusPatient ourInstance = new SingletonStatusPatient();
    public static SingletonStatusPatient getInstance() { return ourInstance; }

    public SingletonStatusPatient() {
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
