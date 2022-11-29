package com.example.courseplannerapp;

import java.util.ArrayList;

class Student {

    private String email;
    private String password;
    private String username;
//    private ArrayList<Courses> takenCourses;
//    private ArrayList<Courses> futureCourses

    protected Student(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
