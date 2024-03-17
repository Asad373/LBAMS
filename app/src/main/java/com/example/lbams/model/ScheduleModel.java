package com.example.lbams.model;

public class ScheduleModel {
    public ScheduleModel(){}

    public ScheduleModel(String classCode, String className, String day, String date, String time) {
        this.classCode = classCode;
        this.className = className;
        Day = day;
        Date = date;
        Time = time;
    }

    public String classCode;
    public String className;
    public String Day;
    public String Date;
    public String Time;
}
