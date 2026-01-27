package com.CryptoProject.CryptoInfosys.controller;

import com.CryptoProject.CryptoInfosys.security.JwtUtils;
import com.CryptoProject.CryptoInfosys.service.HoldingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/holdings")
@CrossOrigin(origins = "http://localhost:3000")
public class HoldingController {

    private final HoldingService holdingService;
    private final JwtUtils jwtUtils;

    public HoldingController(HoldingService holdingService, JwtUtils jwtUtils) {
        this.holdingService = holdingService;
        this.jwtUtils = jwtUtils;
    }

    // ✅ READ-ONLY holdings (derived from trades)
    @GetMapping
    public ResponseEntity<?> getHoldings(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtils.extractUsername(token);

        return ResponseEntity.ok(holdingService.getHoldings(email));
    }
}
