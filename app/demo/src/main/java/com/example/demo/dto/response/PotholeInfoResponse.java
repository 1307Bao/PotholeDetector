package com.example.demo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PotholeInfoResponse {
    int totalPotholeDetected;
    int totalPotholeEncounter;
    int todayPothole;
}