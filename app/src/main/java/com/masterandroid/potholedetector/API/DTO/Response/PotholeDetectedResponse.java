package com.masterandroid.potholedetector.API.DTO.Response;

import java.util.Date;

public class PotholeDetectedResponse {
    private String id;
    private double longitude;
    private double latitude;
    private String address;
    private Date timeDetected;

    public PotholeDetectedResponse() {}

    public PotholeDetectedResponse(String id, double longitude, double latitude, String address, Date timeDetected) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.timeDetected = timeDetected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getTimeDetected() {
        return timeDetected;
    }

    public void setTimeDetected(Date timeDetected) {
        this.timeDetected = timeDetected;
    }
}
