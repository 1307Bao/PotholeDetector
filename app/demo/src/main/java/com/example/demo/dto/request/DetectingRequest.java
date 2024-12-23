package com.example.demo.dto.request;

import com.example.demo.dto.Point;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DetectingRequest {
    @JsonProperty("potholeRequests")
    List<PotholeRequest> potholeRequests;
    @JsonProperty("currentPoint")
    Point currentPoint;
}
