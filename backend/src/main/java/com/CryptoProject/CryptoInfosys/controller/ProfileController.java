package com.CryptoProject.CryptoInfosys.controller;

import com.CryptoProject.CryptoInfosys.model.User;
import com.CryptoProject.CryptoInfosys.security.JwtUtils;
import com.CryptoProject.CryptoInfosys.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:3000")
public class ProfileController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    public ProfileController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/get-profile")
    public User getProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtils.extractUsername(token);
        return userService.getUserByEmail(email);
    }

    @PostMapping("/update-profile")
    public User updateProfile(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PostMapping("/change-password")
    public String changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String newPassword
    ) {
        String token = authHeader.substring(7);
        String email = jwtUtils.extractUsername(token);
        userService.changePassword(email, newPassword);
        return "Password changed";
    }

    @PostMapping("/set-preferences")
    public String setPreferences(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> preferences
    ) {
        String token = authHeader.substring(7);
        String email = jwtUtils.extractUsername(token);
        userService.setPreferences(email, preferences);
        return "Preferences updated";
    }

    @PostMapping("/delete-account")
    public String deleteAccount(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtils.extractUsername(token);
        userService.deleteUserByEmail(email);
        return "Account deleted";
    }
}
