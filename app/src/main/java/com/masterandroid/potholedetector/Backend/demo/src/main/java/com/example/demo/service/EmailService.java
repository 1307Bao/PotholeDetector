package com.example.demo.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    JavaMailSender javaMailSender;

    public void sendOTPEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Mã xác minh để đặt lại mật khẩu");
        message.setText("Mã xác minh OTP của bạn là: " + otp + "\nOTP này sẽ hết hạn sau 10 phút.");

        javaMailSender.send(message);
    }
}
