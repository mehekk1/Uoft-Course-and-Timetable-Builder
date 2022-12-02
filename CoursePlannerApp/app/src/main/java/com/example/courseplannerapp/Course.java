package com.example.courseplannerapp;

import java.util.List;

public class Course {
    String name;
    String code;
    List<Boolean> offerings;
    List<String> prereqs;

    public Course() {
    }

    public Course(String name, String code, List<Boolean> offerings, List<String> prereqs) {
        this.name = name;
        this.code = code;
        this.offerings = offerings;
        this.prereqs = prereqs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Boolean> getOfferings() {
        return offerings;
    }

    public void setOfferings(List<Boolean> offerings) {
        this.offerings = offerings;
    }

    public List<String> getPrereqs() {
        return prereqs;
    }

    public void setPrereqs(List<String> prereqs) {
        this.prereqs = prereqs;
    }
}
