package com.example.courseplannerapp;

public class CourseSearchItem {

    private String code;
    private boolean selected;

    public CourseSearchItem(String code, boolean selected) {
        this.code = code;
        this.selected = selected;
    }

    public String getCode() {
        return code;
    }

    public boolean getSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

