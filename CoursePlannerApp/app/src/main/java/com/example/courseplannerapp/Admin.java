package com.example.courseplannerapp;

class Admin {

    private String email;
    private String password;
    private String username;

    protected Admin(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
