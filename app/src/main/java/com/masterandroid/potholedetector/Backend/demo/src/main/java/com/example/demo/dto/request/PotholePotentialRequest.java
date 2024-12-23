package com.example.demo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PotholePotentialRequest {
    String id;
    double longitude;
    double latitude;
    String address;
    Date timeDetected;
}
