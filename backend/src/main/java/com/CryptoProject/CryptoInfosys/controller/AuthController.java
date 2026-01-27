// src/main/java/com/CryptoProject/CryptoInfosys/controller/AuthController.java
package com.CryptoProject.CryptoInfosys.controller;

import com.CryptoProject.CryptoInfosys.dto.AuthRequest;
import com.CryptoProject.CryptoInfosys.dto.AuthResponse;
import com.CryptoProject.CryptoInfosys.dto.ResetPasswordRequest;
import com.CryptoProject.CryptoInfosys.dto.ForgotPasswordRequest;
import com.CryptoProject.CryptoInfosys.dto.RegisterRequest;
import com.CryptoProject.CryptoInfosys.service.AuthService;
import com.CryptoProject.CryptoInfosys.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {
    private final AuthService authService;
    private final MailService mailService;

    public AuthController(AuthService authService, MailService mailService) {
        this.authService = authService;
        this.mailService = mailService;
    }

    @PostMapping("/register")
public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
    authService.register(req);

    try {
        mailService.sendEmail(
            req.getEmail(),
            "Welcome",
            "Your account has been created."
        );
    } catch (Exception e) {
        // log only, DO NOT fail registration
        System.err.println("Mail failed: " + e.getMessage());
    }

    return ResponseEntity.ok("User registered successfully");
}


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        AuthResponse resp = authService.login(req);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/forgot-password")
public ResponseEntity<?> forgotPassword(
        @RequestBody ForgotPasswordRequest request) {

    authService.forgotPassword(request.getEmail());

    return ResponseEntity.ok(
            Map.of("message", "Reset link sent successfully"));
}


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        authService.resetPassword(request);

        return ResponseEntity.ok(
                Map.of("message", "Password reset successfully"));
    }
}
