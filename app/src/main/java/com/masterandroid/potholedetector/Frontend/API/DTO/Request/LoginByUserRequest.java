package com.masterandroid.potholedetector.Frontend.API.DTO.Request;

public class LoginByUserRequest {
    private String email;
    private String password;

    public LoginByUserRequest() {}

    public LoginByUserRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
