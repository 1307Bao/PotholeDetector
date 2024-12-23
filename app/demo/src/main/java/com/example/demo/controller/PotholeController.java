package com.example.demo.controller;

import com.example.demo.dto.Point;
import com.example.demo.dto.request.*;
import com.example.demo.dto.response.PotholePotentialResponse;
import com.example.demo.dto.response.PotholeResponse;
import com.example.demo.entity.PotholePotential;
import com.example.demo.exception.AppRunTimeException;
import com.example.demo.service.PotholeService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/potholes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PotholeController {
    PotholeService potholeService;

    @PostMapping("create")
    public ApiResponse<Void> createPothole(
            @RequestParam String id
            ) throws AppRunTimeException, ParseException, JOSEException {

        potholeService.createPothole(id);

        return ApiResponse.<Void>builder().build();
    }

    @PatchMapping("delete")
    public ApiResponse<Void> deletePotentialPothole(@RequestParam String id){
        potholeService.deletePothole(id);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping
    public ApiResponse<List<PotholeResponse>> getPotholes() {
        List<PotholeResponse> result = potholeService.getPotholes();

        return ApiResponse.<List<PotholeResponse>>builder()
                .result(result).build();
    }

    @PostMapping("/potholes-on-map")
    public ApiResponse<List<PotholeResponse>> getAllPotholeOnMap(@RequestBody List<RouteRequest> requests) {
        List<PotholeResponse> result = potholeService.getAllPotholeOnMap(requests);

        return ApiResponse.<List<PotholeResponse>>builder().result(result).build();
    }

    @PostMapping("met-pothole")
    public void metPothole(@RequestBody PotholeRequest request) {
        potholeService.metPothole(request);
    }

    @PostMapping("insert-potential-pothole")
    public void addMorePotentialPothole(@RequestParam double longitude,
                                        @RequestParam double latitude) {
        potholeService.addMorePotentialPothole(longitude, latitude);
    }

    @GetMapping("all-pothole-potential")
    public List<PotholePotentialResponse> getAll() {
        try {
            potholeService.getAll();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return potholeService.getAll();
    }
}
