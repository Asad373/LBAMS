package com.example.lbams.model;

public class AttendanceLocationList {
    public  AttendanceLocationList(){}
    public AttendanceLocationList(double lati, double longi, String classCode) {
        this.lati = lati;
        this.longi = longi;
        this.ClassCode = classCode;
    }

    public String ClassCode;
    public  double lati;
    public  double longi;
}

