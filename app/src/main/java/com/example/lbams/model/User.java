package com.example.lbams.model;

public class User {
    public User(){}
    public User(String firstName, String lastName, String email, String password, String role, String courseCode, String ft) {
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        pass = password;
        Role = role;
        CourseCode = courseCode;
        FT = ft;
    }

    public  String FirstName;
    public  String LastName;
    public  String Email;
    public  String pass;
    public  String Role;
    public  String CourseCode;
    public  String FT;
}
