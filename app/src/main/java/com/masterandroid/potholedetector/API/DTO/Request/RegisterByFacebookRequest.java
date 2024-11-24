package com.masterandroid.potholedetector.API.DTO.Request;

public class RegisterByFacebookRequest {
    private String name;
    private String email;

    public RegisterByFacebookRequest() {}

    public RegisterByFacebookRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
