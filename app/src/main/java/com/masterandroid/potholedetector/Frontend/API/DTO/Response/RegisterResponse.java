package com.masterandroid.potholedetector.Frontend.API.DTO.Response;

public class RegisterResponse {
    private String token;
    private String username;
    private String name;

    public RegisterResponse() {}

    public RegisterResponse(String token, String username, String name) {
        this.token = token;
        this.username = username;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

