package com.example.demo.service;

import com.example.demo.entity.PasswordResetOTP;
import com.example.demo.repository.PasswordResetOTPRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordResetOTPService {
    PasswordResetOTPRepository passwordResetOTPRepository;

    public String generateOTP(String email) {
        var otp = String.valueOf((int) (Math.random() * 90000) + 10000);

        PasswordResetOTP passwordResetOTP = passwordResetOTPRepository.findByEmail(email);
        if (passwordResetOTP == null) {
            passwordResetOTP = new PasswordResetOTP();
            passwordResetOTP.setEmail(email);
        }

        passwordResetOTP.setOtp(otp);
        passwordResetOTP.setExpiryDate(LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
        passwordResetOTPRepository.save(passwordResetOTP);

        return otp;
    }

    public boolean validatedOTP(String email, String otp) {
        PasswordResetOTP passwordResetOtp = passwordResetOTPRepository.findByEmail(email);
        if (passwordResetOtp == null || !passwordResetOtp.getOtp().equals(otp) ||
                passwordResetOtp.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }
}
