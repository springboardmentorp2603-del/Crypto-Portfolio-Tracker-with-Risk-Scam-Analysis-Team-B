package com.CryptoProject.CryptoInfosys.controller;

import com.CryptoProject.CryptoInfosys.dto.TaxSummaryDTO;
import com.CryptoProject.CryptoInfosys.model.User;
import com.CryptoProject.CryptoInfosys.repository.UserRepository;
import com.CryptoProject.CryptoInfosys.security.JwtUtils;
import com.CryptoProject.CryptoInfosys.service.TaxService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tax")
@CrossOrigin(origins = "http://localhost:3000")
public class TaxController {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepo;
    private final TaxService taxService;

    public TaxController(
            JwtUtils jwtUtils,
            UserRepository userRepo,
            TaxService taxService
    ) {
        this.jwtUtils = jwtUtils;
        this.userRepo = userRepo;
        this.taxService = taxService;
    }

    @GetMapping("/hints")
    public TaxSummaryDTO getTaxHints(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtils.extractUsername(token);

        User user = userRepo.findByEmail(email).orElseThrow();

        return taxService.calculateTaxSummary(user.getId());
    }
}
