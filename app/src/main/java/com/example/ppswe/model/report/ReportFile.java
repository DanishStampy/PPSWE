package com.example.ppswe.model.report;

import java.util.ArrayList;

public class ReportFile {
    String patientName;
    String caregiverName;
    ArrayList<String> reportDate;
    ArrayList<String> medStatus;
    ArrayList<String> medTimes;
    ArrayList<String> medName;

    public ReportFile() {
    }

    public ReportFile(ArrayList<String> reportDate, ArrayList<String> medStatus, ArrayList<String> medTimes, ArrayList<String> medName) {
        this.reportDate = reportDate;
        this.medStatus = medStatus;
        this.medTimes = medTimes;
        this.medName = medName;
    }

    public ArrayList<String> getMedName() {
        return medName;
    }

    public void setMedName(ArrayList<String> medName) {
        this.medName = medName;
    }

    public ArrayList<String> getMedTimes() {
        return medTimes;
    }

    public void setMedTimes(ArrayList<String> medTimes) {
        this.medTimes = medTimes;
    }

    public ArrayList<String> getMedStatus() {
        return medStatus;
    }

    public void setMedStatus(ArrayList<String> medStatus) {
        this.medStatus = medStatus;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getCaregiverName() {
        return caregiverName;
    }

    public void setCaregiverName(String caregiverName) {
        this.caregiverName = caregiverName;
    }

    public ArrayList<String> getReportDate() {
        return reportDate;
    }

    public void setReportDate(ArrayList<String> reportDate) {
        this.reportDate = reportDate;
    }


}
