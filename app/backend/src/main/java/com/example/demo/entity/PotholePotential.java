    package com.example.demo.entity;

    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import lombok.*;
    import lombok.experimental.FieldDefaults;

    import java.util.Date;

    @Entity
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public class PotholePotential {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        String id;
        double longitude;
        double latitude;
        Date timeDetected;
        String address;
        String userId;
    }
