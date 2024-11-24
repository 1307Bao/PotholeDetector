package com.masterandroid.potholedetector.API.DTO.Request;

public class LoginByFacebookRequest {
    private String name;
    private String email;

    public LoginByFacebookRequest() {}

    public LoginByFacebookRequest(String name, String email) {
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
