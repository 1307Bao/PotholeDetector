package com.masterandroid.potholedetector.Frontend.API.DTO.Request;

public class IntrospectRequest {
    private String token;

    public IntrospectRequest(String token) {
        this.token = token;
    }

    public IntrospectRequest() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
