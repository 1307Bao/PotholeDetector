package com.masterandroid.potholedetector.Frontend.API.DTO.Response;

public class PotholeInfoResponse {
    private int totalPotholeDetected;
    private int totalPotholeEncounter;
    private int todayPothole;

    public PotholeInfoResponse() {}

    public PotholeInfoResponse(int totalPotholeDetected, int totalPotholeEncounter, int todayPothole) {
        this.totalPotholeDetected = totalPotholeDetected;
        this.totalPotholeEncounter = totalPotholeEncounter;
        this.todayPothole = todayPothole;
    }

    public int getTotalPotholeDetected() {
        return totalPotholeDetected;
    }

    public void setTotalPotholeDetected(int totalPotholeDetected) {
        this.totalPotholeDetected = totalPotholeDetected;
    }

    public int getTotalPotholeEncounter() {
        return totalPotholeEncounter;
    }

    public void setTotalPotholeEncounter(int totalPotholeEncounter) {
        this.totalPotholeEncounter = totalPotholeEncounter;
    }

    public int getTodayPothole() {
        return todayPothole;
    }

    public void setTodayPothole(int todayPothole) {
        this.todayPothole = todayPothole;
    }
}
