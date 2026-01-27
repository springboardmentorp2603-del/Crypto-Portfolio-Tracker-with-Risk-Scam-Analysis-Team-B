package com.CryptoProject.CryptoInfosys.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendAccountCreatedEmail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Account Created Successfully");
        message.setText("Welcome " + username + "! Your account has been created.");
        mailSender.send(message);
    }

    public void sendLoginAlertEmail(String to, LocalDateTime timestamp, String ipAddress) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Login Alert");
        message.setText("You logged in at " + timestamp + " from IP: " + ipAddress);
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String to, String token) {

    String resetUrl = "http://localhost:3000/reset-password?token="+token;

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject("Password Reset Request");
    message.setText("Click here to reset your password: \n\n" + resetUrl);

    mailSender.send(message);
}

}
