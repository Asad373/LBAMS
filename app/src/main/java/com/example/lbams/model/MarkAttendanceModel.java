package com.example.lbams.model;

public class MarkAttendanceModel {

    public MarkAttendanceModel(){}


    public MarkAttendanceModel(String id,String isCheckedIn, String isCheckedOut, String date, String time, String classCode, String email, String isApproved) {
        this.Id = id;
        this.isCheckedIn = isCheckedIn;
        this.isCheckedOut = isCheckedOut;
        this.date = date;
        this.time = time;
        this.ClassCode  = classCode;
        this.Email = email;
        this.isApproved = isApproved;
    }

    public String Id;
    public String isCheckedIn;
    public String isCheckedOut;
    public String date;
    public String time;
    public String ClassCode;
    public String Email;
    public String isApproved;

}
