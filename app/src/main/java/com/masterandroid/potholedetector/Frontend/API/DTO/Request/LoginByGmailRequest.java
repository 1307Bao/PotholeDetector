package com.masterandroid.potholedetector.Frontend.API.DTO.Request;

public class LoginByGmailRequest {
    private String token;

    public LoginByGmailRequest(String token) {
        this.token = token;
    }

    public LoginByGmailRequest() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
