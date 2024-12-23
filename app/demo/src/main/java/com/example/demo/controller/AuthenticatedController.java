package com.example.demo.controller;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.dto.request.LogoutRequest;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.exception.AppRunTimeException;
import com.example.demo.service.AuthenticatedService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticatedController {

    AuthenticatedService authenticatedService;

    @PostMapping("/introspect")
    public ApiResponse<AuthenticationResponse> introspect(@RequestBody IntrospectRequest request) {
        AuthenticationResponse result = authenticatedService.introspect(request);

        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    public ApiResponse<AuthenticationResponse> logout(@RequestBody LogoutRequest request) throws AppRunTimeException, ParseException, JOSEException {
        authenticatedService.logout(request);
        return ApiResponse.<AuthenticationResponse>builder().build();
    }
}
