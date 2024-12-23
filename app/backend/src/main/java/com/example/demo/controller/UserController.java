package com.example.demo.controller;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.response.PotholeDetectedResponse;
import com.example.demo.dto.response.PotholeInfoResponse;
import com.example.demo.dto.response.ReportResponse;
import com.example.demo.exception.AppRunTimeException;
import com.example.demo.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping("my-pothole-info")
    PotholeInfoResponse getMyPotholeInfo() {
        return userService.getMyPotholeInfo();
    }

    @GetMapping("my-pothole-detected")
    List<PotholeDetectedResponse> getMyPotholeDetected() {
        return userService.getMyPotholeDetected();
    }

    @GetMapping("report")
    ReportResponse getReport() {
        try {
            return userService.getReport();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PutMapping("update")
    ApiResponse<Void> updateUser(@RequestParam String username, @RequestParam String name) throws AppRunTimeException {
        userService.updateUser(username, name);
        return ApiResponse.<Void>builder().build();
    }
}
