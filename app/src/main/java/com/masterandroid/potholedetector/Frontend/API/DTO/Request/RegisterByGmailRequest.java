package com.masterandroid.potholedetector.Frontend.API.DTO.Request;

public class RegisterByGmailRequest {
    private String token;

    public RegisterByGmailRequest(String token) {
        this.token = token;
    }

    public RegisterByGmailRequest() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
