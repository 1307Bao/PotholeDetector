package com.masterandroid.potholedetector.Frontend.Model;

public class PotholeModel {

    private String status, datetime, address;

    public PotholeModel() {
    }

    public PotholeModel(String status, String datetime, String address) {
        this.status = status;
        this.datetime = datetime;
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
