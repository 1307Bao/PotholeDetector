package com.example.demo.service;

import com.example.demo.dto.response.PotholeDetectedResponse;
import com.example.demo.dto.response.PotholeInfoResponse;
import com.example.demo.dto.response.ReportPerDayResponse;
import com.example.demo.dto.response.ReportResponse;
import com.example.demo.entity.Pothole;
import com.example.demo.exception.AppRunTimeException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.PotholeDetectedRepository;
import com.example.demo.repository.PotholeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {
    PotholeDetectedRepository potholeDetectedRepository;
    PotholeRepository potholeRepository;

    public PotholeInfoResponse getMyPotholeInfo() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        int totalPotholeDetected = potholeRepository.totalPotholeDetected(userId);
        int totalPotholeEncounter = potholeDetectedRepository.totalPotholeEncountered(userId);
        int todayPotholeDetected = potholeRepository.todayPotholeDetected(userId);

        return PotholeInfoResponse.builder()
                .totalPotholeEncounter(totalPotholeEncounter)
                .todayPothole(todayPotholeDetected)
                .totalPotholeDetected(totalPotholeDetected)
                .build();
    }

    public List<PotholeDetectedResponse> getMyPotholeDetected() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Pothole> list = potholeRepository.getCurrentPothole(userId);

        return list.stream().map(
                pothole -> PotholeDetectedResponse.builder()
                        .id(pothole.getId())
                        .address(pothole.getAddress())
                        .timeDetected(pothole.getTimeDetected())
                        .latitude(pothole.getLatitude())
                        .longitude(pothole.getLongitude())
                        .build()
        ).toList();
    }

    public ReportResponse getReport() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // Lấy dữ liệu pothole detected và encountered theo từng ngày
        List<Object[]> listDetectedInfoPerDay = potholeRepository.getInfoPerDay(userId);
        List<Object[]> listEncounteredPerDay = potholeDetectedRepository.getInfoPerDay(userId);

        List<ReportPerDayResponse> reportPerDayResponses = new ArrayList<>();

        Map<Integer, Integer> detectedMap = new HashMap<>();
        Map<Integer, Integer> encounteredMap = new HashMap<>();

        for (Object[] row : listDetectedInfoPerDay) {
            int dayOrder = ((Number) row[1]).intValue(); // DAYOFWEEK
            int detectedCount = ((Number) row[2]).intValue(); // COUNT(*)
            detectedMap.put(dayOrder, detectedCount);
        }

        for (Object[] row : listEncounteredPerDay) {
            int dayOrder = ((Number) row[1]).intValue();
            int encounteredCount = ((Number) row[2]).intValue();
            encounteredMap.put(dayOrder, encounteredCount);
        }

        for (int i = 1; i <= 7; i++) { // Thứ 2 (2) -> Chủ nhật (8)
            int totalPotholeDetected = detectedMap.getOrDefault(i, 0);
            int totalPotholeEncountered = encounteredMap.getOrDefault(i, 0);
            int total = totalPotholeDetected + totalPotholeEncountered;

            ReportPerDayResponse report = ReportPerDayResponse.builder()
                    .totalPotholeDetected(totalPotholeDetected)
                    .totalPotholeEncountered(totalPotholeEncountered)
                    .total(total)
                    .build();

            reportPerDayResponses.add(report);
        }

        List<Object[]> potholeIds = potholeDetectedRepository.getMostEncounteredPothole(userId);
        List<Pothole> potholes = potholeIds.stream().map(
                potholeId -> {
                    try {
                        return potholeRepository.findById(potholeId[0].toString())
                                .orElseThrow(() -> new AppRunTimeException(ErrorCode.UNCATEGORIZED_EXCEPTION));
                    } catch (AppRunTimeException e) {
                        throw new RuntimeException(e);
                    }
                }
                ).toList();

        List<PotholeDetectedResponse> potholeDetectedResponses = potholes.stream().map(
                pothole -> PotholeDetectedResponse.builder()
                        .address(pothole.getAddress())
                        .id(pothole.getId())
                        .timeDetected(pothole.getTimeDetected())
                        .build()
        ).toList();

        return ReportResponse.builder()
                .reportPerDayResponses(reportPerDayResponses)
                .potholeDetectedResponses(potholeDetectedResponses)
                .build();
    }
}