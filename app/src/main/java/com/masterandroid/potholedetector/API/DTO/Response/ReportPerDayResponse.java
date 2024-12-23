package com.masterandroid.potholedetector.API.DTO.Response;

public class ReportPerDayResponse {
    private int totalPotholeDetected;
    private int totalPotholeEncountered;
    private int total;

    public ReportPerDayResponse() {}

    public ReportPerDayResponse(int totalPotholeDetected, int totalPotholeEncountered, int total) {
        this.totalPotholeDetected = totalPotholeDetected;
        this.totalPotholeEncountered = totalPotholeEncountered;
        this.total = total;
    }

    public int getTotalPotholeDetected() {
        return totalPotholeDetected;
    }

    public void setTotalPotholeDetected(int totalPotholeDetected) {
        this.totalPotholeDetected = totalPotholeDetected;
    }

    public int getTotalPotholeEncountered() {
        return totalPotholeEncountered;
    }

    public void setTotalPotholeEncountered(int totalPotholeEncountered) {
        this.totalPotholeEncountered = totalPotholeEncountered;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
