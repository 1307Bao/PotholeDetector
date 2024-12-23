package com.example.demo.controller;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.request.LoginByFacebookRequest;
import com.example.demo.dto.request.LoginByGmailRequest;
import com.example.demo.dto.request.LoginByUserRequest;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.exception.AppRunTimeException;
import com.example.demo.service.LoginService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginController {
    LoginService loginService;
    
    @PostMapping("/user")
    public ApiResponse<LoginResponse> loginByUser(@RequestBody LoginByUserRequest request)
            throws AppRunTimeException {
        LoginResponse result = loginService.loginByUser(request);

        return ApiResponse.<LoginResponse>builder()
                .result(result)
                .message("Login success")
                .code(200)
                .build();
    }

    @PostMapping("/gmail")
    public ApiResponse<LoginResponse> loginByGmail(@RequestBody LoginByGmailRequest request) throws AppRunTimeException {
        LoginResponse loginResponse = loginService.loginByGmail(request);

        return ApiResponse.<LoginResponse>builder()
                .result(loginResponse)
                .message("Login sucess")
                .code(200)
                .build();
    }

    @PostMapping("/facebook")
    public ApiResponse<LoginResponse> loginByFacebook(@RequestBody LoginByFacebookRequest request) throws AppRunTimeException {
        LoginResponse loginResponse = loginService.loginByFacebook(request);

        return ApiResponse.<LoginResponse>builder()
                .result(loginResponse)
                .message("Login sucess")
                .code(200)
                .build();
    }
}
