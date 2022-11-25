package com.example.courseplannerapp;

import java.util.List;

public class Course {
    String name;
    String code;
    List<String> offerings;
    List<Course> pre_reqs;

    public Course() {
    }

    public Course(String name, String code, List<String> offerings, List<Course> pre_reqs) {
        this.name = name;
        this.code = code;
        this.offerings = offerings;
        this.pre_reqs = pre_reqs;
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

    public List<String> getOfferings() {
        return offerings;
    }

    public void setOfferings(List<String> offerings) {
        this.offerings = offerings;
    }

    public List<Course> getPre_reqs() {
        return pre_reqs;
    }

    public void setPre_reqs(List<Course> pre_reqs) {
        this.pre_reqs = pre_reqs;
    }
}
