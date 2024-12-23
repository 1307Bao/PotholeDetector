package com.example.demo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportResponse {
    List<ReportPerDayResponse> reportPerDayResponses;
    List<PotholeDetectedResponse> potholeDetectedResponses;
}
