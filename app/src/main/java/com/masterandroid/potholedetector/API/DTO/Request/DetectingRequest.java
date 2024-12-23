package com.masterandroid.potholedetector.API.DTO.Request;

import com.masterandroid.potholedetector.API.DTO.Point;

import java.util.List;

public class DetectingRequest {
    private List<PotholeRequest> requests;
    private Point currentPoint;

    public DetectingRequest(List<PotholeRequest> requests, Point currentPoint) {
        this.requests = requests;
        this.currentPoint = currentPoint;
    }

    public List<PotholeRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<PotholeRequest> requests) {
        this.requests = requests;
    }

    public Point getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(Point currentPoint) {
        this.currentPoint = currentPoint;
    }
}
