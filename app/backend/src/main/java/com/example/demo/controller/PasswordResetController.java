package com.example.demo.controller;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.request.ResetPasswordRequest;
import com.example.demo.entity.User;
import com.example.demo.exception.AppRunTimeException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.PasswordResetOTPService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordResetController {
    PasswordResetOTPService passwordResetOTPService;
    UserRepository userRepository;
    EmailService emailService;
    PasswordEncoder passwordEncoder;

    @PostMapping("/forgot-password")
    public ApiResponse<?> sendOtp(@RequestParam String email) throws AppRunTimeException {
        if (!userRepository.existsByEmail(email)) {
            throw new AppRunTimeException(ErrorCode.USER_NOT_EXISTED);
        }

        String otp = passwordResetOTPService.generateOTP(email);
        emailService.sendOTPEmail(email, otp);

        return ApiResponse.builder()
                .code(200)
                .message("Success").build();
    }

    @PostMapping("/validate-otp")
    public ApiResponse<?> validateOtp(@RequestParam String email, @RequestParam String otp) throws AppRunTimeException {
        boolean isValidate = passwordResetOTPService.validatedOTP(email, otp);

        if (!isValidate) {
            throw new AppRunTimeException(ErrorCode.INVALID_OTP);
        }

        return ApiResponse.builder()
                .code(200)
                .message("Success").build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<?> resetPassword(@RequestBody ResetPasswordRequest request) throws AppRunTimeException {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new AppRunTimeException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return ApiResponse.builder()
                .code(200)
                .message("Success")
                .result("Password was changed").build();
    }
}
