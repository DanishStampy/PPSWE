package com.example.ppswe.model;

public class User {

    private String username;
    private String email;
    private String phoneNumber;
    private String roles;

    public User() {
    }

    public User( String username, String email, String phoneNumber, String roles) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }


    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRoles() {
        return roles;
    }
}
