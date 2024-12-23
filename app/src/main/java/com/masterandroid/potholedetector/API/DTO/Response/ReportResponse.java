package com.masterandroid.potholedetector.API.DTO.Response;

import java.util.List;

public class ReportResponse {
    private List<ReportPerDayResponse> reportPerDayResponses;
    private List<PotholeDetectedResponse> potholeDetectedResponses;

    public ReportResponse() {}

    public ReportResponse(List<ReportPerDayResponse> reportPerDayResponses, List<PotholeDetectedResponse> potholeDetectedResponses) {
        this.reportPerDayResponses = reportPerDayResponses;
        this.potholeDetectedResponses = potholeDetectedResponses;
    }

    public List<ReportPerDayResponse> getReportPerDayResponses() {
        return reportPerDayResponses;
    }

    public void setReportPerDayResponses(List<ReportPerDayResponse> reportPerDayResponses) {
        this.reportPerDayResponses = reportPerDayResponses;
    }

    public List<PotholeDetectedResponse> getPotholeDetectedResponses() {
        return potholeDetectedResponses;
    }

    public void setPotholeDetectedResponses(List<PotholeDetectedResponse> potholeDetectedResponses) {
        this.potholeDetectedResponses = potholeDetectedResponses;
    }
}
