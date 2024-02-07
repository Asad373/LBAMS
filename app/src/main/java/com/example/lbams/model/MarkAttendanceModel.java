package com.example.lbams.model;

public class MarkAttendanceModel {

    public MarkAttendanceModel(){}


    public MarkAttendanceModel(String isCheckedIn, String isCheckedOut, String date, String time) {
        this.isCheckedIn = isCheckedIn;
        this.isCheckedOut = isCheckedOut;
        this.date = date;
        this.time = time;
    }

    public String isCheckedIn;
    public String isCheckedOut;
    public String date;
    public String time;

}
