    package com.example.demo.entity;

    import jakarta.persistence.*;
    import lombok.*;
    import lombok.experimental.FieldDefaults;

    import java.time.Instant;
    import java.util.Date;

    @Entity
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public class Pothole {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        String id;
        double longitude;
        double latitude;
        Date timeDetected;
        String address;
        String userId;
    }
