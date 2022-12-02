package com.example.courseplannerapp;

import java.util.List;

public class User {

    boolean isStudent;
    List<String> taken_list;

    public User(){

    }
    public User(boolean isStudent, List<String> taken){
        this.isStudent = isStudent;
        taken_list = taken;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public List<String> getTaken_list() {
        return taken_list;
    }
}
