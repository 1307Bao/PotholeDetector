package com.masterandroid.potholedetector.Frontend.API.DTO.Response;

public class RegisterResponse {
    private String token;

    public RegisterResponse() {}

    public RegisterResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

