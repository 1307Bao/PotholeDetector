package com.masterandroid.potholedetector.Frontend.API.DTO.Response;

public class AuthenticationResponse {
    private boolean valid;

    public AuthenticationResponse() {}

    public AuthenticationResponse(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
