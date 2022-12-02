//package com.example.courseplannerapp;
//
//import java.util.List;
//
//public class Course {
//    String name;
//    String code;
//    boolean fall;
//    boolean winter;
//    boolean summer;
//    List<Course> prereqs;
//
//    public Course() {
//    }
//
//    public Course(String name, String code, boolean fall, boolean winter, boolean summer, List<Course> prereqs) {
//        this.name = name;
//        this.code = code;
//        this.fall = fall;
//        this.winter = winter;
//        this.summer = summer;
//        this.prereqs = prereqs;
//    }
//
//    public Course(String name, String code, boolean fall, boolean winter, boolean summer) {
//        this.name = name;
//        this.code = code;
//        this.fall = fall;
//        this.winter = winter;
//        this.summer = summer;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public boolean isFall() {
//        return fall;
//    }
//
//    public void setFall(boolean fall) {
//        this.fall = fall;
//    }
//
//    public boolean isWinter() {
//        return winter;
//    }
//
//    public void setWinter(boolean winter) {
//        this.winter = winter;
//    }
//
//    public boolean isSummer() {
//        return summer;
//    }
//
//    public void setSummer(boolean summer) {
//        this.summer = summer;
//    }
//
//    public List<Course> getPrereqs() {
//        return prereqs;
//    }
//
//    public void setPrereqs(List<Course> prereqs) {
//        this.prereqs = prereqs;
//    }
//}
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