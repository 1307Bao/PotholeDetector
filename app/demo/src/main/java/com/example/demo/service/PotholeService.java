package com.example.demo.service;

import com.example.demo.dto.Point;
import com.example.demo.dto.request.PotholePotentialRequest;
import com.example.demo.dto.request.PotholeRequest;
import com.example.demo.dto.request.RouteRequest;
import com.example.demo.dto.response.PotholePotentialResponse;
import com.example.demo.dto.response.PotholeResponse;
import com.example.demo.entity.Pothole;
import com.example.demo.entity.PotholeDetected;
import com.example.demo.entity.PotholePotential;
import com.example.demo.entity.User;
import com.example.demo.exception.AppRunTimeException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.PotholeMapper;
import com.example.demo.repository.*;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageResponse;
import com.opencagedata.jopencage.model.JOpenCageReverseRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PotholeService {
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SECRET_KEY;

    @NonFinal
    @Value("${jwt.geocoding-api}")
    protected String GEO_API;

    PotholeRepository potholeRepository;
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    PotholeMapper potholeMapper;
    PotholeDetectedRepository potholeDetectedRepository;
    PotholePotentialRepository potholePotentialRepository;

    int THRESH_HOLD = 5;

    public void createPothole(String id) throws AppRunTimeException, ParseException, JOSEException {
        PotholePotential request = potholePotentialRepository.findById(id).orElseThrow(
                () -> new AppRunTimeException(ErrorCode.UNCATEGORIZED_EXCEPTION)
        );
        if (potholeRepository.existsByLongitudeAndLatitude(request.getLongitude(), request.getLatitude())) {
            potholePotentialRepository.deleteById(request.getId());
            return;
        }

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        String address = request.getAddress();

        Pothole pothole = Pothole.builder().latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .timeDetected(request.getTimeDetected())
                .userId(userId)
                .address(address).build();

        potholePotentialRepository.deleteById(id);
        potholeRepository.save(pothole);
    }

    public void deletePothole(String id) {
        potholePotentialRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('USER')")
    public List<PotholeResponse> getPotholes() {
        return potholeRepository.findAll().stream()
                .map(potholeMapper::toPotholeResponse).toList();
    }

    @PreAuthorize("hasRole('USER')")
    public List<PotholeResponse> getAllPotholeOnMap(List<RouteRequest> requests) {
        List<PotholeResponse> allPotholes = getPotholes();
        List<PotholeResponse> potholesMaybeOnMap = getPotholesMaybeOnMap(allPotholes, requests);
        List<PotholeResponse> potholesOnMap = getPotholesOnMap(potholesMaybeOnMap, requests);

        Set<PotholeResponse> uniquePotholes = new HashSet<>(potholesOnMap);
        potholesOnMap.clear();
        potholesOnMap.addAll(uniquePotholes);

        return potholesOnMap;
    }

    private List<PotholeResponse> getPotholesMaybeOnMap(List<PotholeResponse> potholeResponses, List<RouteRequest> requests) {
        double minLat = 1e9;
        double maxLat = -1e9;
        double minLong = 1e9;
        double maxLong = -1e9;

        for (RouteRequest waypoint : requests) {
            minLat = Math.min(waypoint.getLatitude(), minLat);
            maxLat = Math.max(waypoint.getLatitude(), maxLat);
            minLong = Math.min(waypoint.getLongitude(), minLong);
            maxLong = Math.max(waypoint.getLongitude(), maxLong);
        }

        List<PotholeResponse> potholeMaybeOnRoute = new ArrayList<>();
        for (PotholeResponse point : potholeResponses) {
            if (point.getLatitude() >= minLat && point.getLatitude() <= maxLat) {
                if (point.getLongitude() >= minLong && point.getLongitude() <= maxLong) {
                    potholeMaybeOnRoute.add(point);
                }
            }
        }

        return potholeMaybeOnRoute;
    }

    private List<PotholeResponse> getPotholesOnMap(List<PotholeResponse> potholeResponses, List<RouteRequest> requests) {
        List<PotholeResponse> potholeOnMap = new ArrayList<>();
        for (int i = 0; i < requests.size() - 1; i++) {
            Point start = new Point(requests.get(i).getLongitude(), requests.get(i).getLatitude());
            Point end = new Point(requests.get(i + 1).getLongitude(), requests.get(i + 1).getLatitude());

            for (PotholeResponse potholeResponse : potholeResponses) {
                Point potholePoint = new Point(potholeResponse.getLongitude(), potholeResponse.getLatitude());
                double distance = pointToSegmentDistance(start, end, potholePoint);

                if (distance <= THRESH_HOLD) { // Ngưỡng khoảng cách (5 mét)
                    potholeOnMap.add(potholeResponse);
                }
            }
        }
        return potholeOnMap;
    }

    private double pointToSegmentDistance(Point start, Point end, Point pothole) {
        double x0 = pothole.getLongitude(), y0 = pothole.getLatitude();
        double x1 = start.getLongitude(), y1 = start.getLatitude();
        double x2 = end.getLongitude(), y2 = end.getLatitude();

        double dx = x2 - x1;
        double dy = y2 - y1;
        double d2 = dx * dx + dy * dy;

        if (d2 == 0) {
            return haversineDistance(start, pothole);
        }

        double t = ((x0 - x1) * dx + (y0 - y1) * dy) / d2;
        t = Math.max(0, Math.min(1, t));

        double closestX = x1 + t * dx;
        double closestY = y1 + t * dy;

        Point closestPoint = new Point(closestX, closestY);

        return haversineDistance(pothole, closestPoint);
    }

    private double haversineDistance(Point p1, Point p2) {
        double R = 6371e3; // Bán kính Trái Đất (mét)
        double dLat = Math.toRadians(p2.getLatitude() - p1.getLatitude());
        double dLon = Math.toRadians(p2.getLongitude() - p1.getLongitude());
        double lat1 = Math.toRadians(p1.getLatitude());
        double lat2 = Math.toRadians(p2.getLatitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    public SignedJWT verifyToken(String token) throws JOSEException, AppRunTimeException {
        try {
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            var verified = signedJWT.verify(verifier);
            if (!(verified && expirationTime.after(new Date()))) {
                throw new AppRunTimeException(ErrorCode.DATE_EXPIRED);
            }

            if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
                throw new AppRunTimeException(ErrorCode.UNAUTHENTICATED);
            }

            return signedJWT;
        } catch (ParseException | JOSEException e) {
            throw new AppRunTimeException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

    }

    public void metPothole(PotholeRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        PotholeDetected potholeDetected = PotholeDetected.builder()
                .potholeId(request.getId())
                .address(request.getAddress())
                .timeMet(new Date())
                .userId(userId)
                .build();

        potholeDetectedRepository.save(potholeDetected);
    }

    public void addMorePotentialPothole(double longitude, double latitude) {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        String address = "";

        JOpenCageGeocoder geocoder = new JOpenCageGeocoder(GEO_API);
        JOpenCageReverseRequest request = new JOpenCageReverseRequest(longitude, latitude);
        request.setLanguage("vi");
        request.setLimit(5);
        request.setNoAnnotations(true);
        request.setMinConfidence(4);

        JOpenCageResponse response = geocoder.reverse(request);
        if (response != null) {
            address = response.getResults().getFirst().getFormatted();
        } else {
            address = "Longitude: " + longitude + ", Latitude: " + latitude;
        }
        PotholePotential pothole = PotholePotential.builder().latitude(latitude)
                .longitude(longitude)
                .timeDetected(new Date())
                .userId(id)
                .address(address).build();
        potholePotentialRepository.save(pothole);
    }

    public List<PotholePotentialResponse> getAll() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<PotholePotential> potholePotentials = potholePotentialRepository.findByUserId(userId);

        return potholePotentials.stream().map(
                potholePotential -> PotholePotentialResponse.builder()
                        .longitude(potholePotential.getLongitude())
                        .address(potholePotential.getAddress())
                        .latitude(potholePotential.getLatitude())
                        .timeDetected(potholePotential.getTimeDetected())
                        .id(potholePotential.getId())
                        .build()
        ).toList();
    }

}
