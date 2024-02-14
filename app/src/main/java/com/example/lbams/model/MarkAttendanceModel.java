package com.example.lbams.model;

public class MarkAttendanceModel {

    public MarkAttendanceModel(){}


    public MarkAttendanceModel(String isCheckedIn, String isCheckedOut, String date, String time, String classCode) {
        this.isCheckedIn = isCheckedIn;
        this.isCheckedOut = isCheckedOut;
        this.date = date;
        this.time = time;
        this.ClassCode  = classCode;
    }

    public String isCheckedIn;
    public String isCheckedOut;
    public String date;
    public String time;
    public String ClassCode;

}
