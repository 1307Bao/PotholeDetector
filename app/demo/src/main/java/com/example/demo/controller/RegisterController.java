package com.example.demo.controller;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.request.RegisterByFacebookRequest;
import com.example.demo.dto.request.RegisterByGmailRequest;
import com.example.demo.dto.request.RegisterByUserRequest;
import com.example.demo.dto.response.RegisterResponse;
import com.example.demo.exception.AppRunTimeException;
import com.example.demo.service.RegisterService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegisterController {

    RegisterService registerService;

    // Register by user
    @PostMapping("/user")
    public ApiResponse<RegisterResponse> registerByUser(@RequestBody @Valid RegisterByUserRequest request)
            throws AppRunTimeException {
        RegisterResponse result = registerService.registerByUser(request);

        return ApiResponse.<RegisterResponse>builder()
                .message("Login success")
                .code(200)
                .result(result)
                .build();
    }

    // Login by gmail
    @PostMapping("/gmail")
    public ApiResponse<RegisterResponse> registerByGoogle(@RequestBody RegisterByGmailRequest request)
            throws AppRunTimeException {
            RegisterResponse result = registerService.registerByGmail(request);

        return ApiResponse.<RegisterResponse>builder()
                .message("Login success")
                .code(200)
                .result(result)
                .build();
    }

    // Login by facebook
    @PostMapping("/facebook")
    public ApiResponse<RegisterResponse> registerByFacebook(@RequestBody RegisterByFacebookRequest request)
            throws AppRunTimeException {
        RegisterResponse result = registerService.registerByFacebook(request);

        return ApiResponse.<RegisterResponse>builder()
                .message("Login success")
                .code(200)
                .result(result)
                .build();
    }
}
