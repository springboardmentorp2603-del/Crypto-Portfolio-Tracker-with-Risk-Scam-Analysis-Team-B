package com.CryptoProject.CryptoInfosys.controller;

import com.CryptoProject.CryptoInfosys.dto.PnLSummaryDTO;
import com.CryptoProject.CryptoInfosys.dto.PnLTimelineDTO;
import com.CryptoProject.CryptoInfosys.model.User;
import com.CryptoProject.CryptoInfosys.repository.UserRepository;
import com.CryptoProject.CryptoInfosys.security.JwtUtils;
import com.CryptoProject.CryptoInfosys.service.PnLService;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pnl")
@CrossOrigin(origins = "http://localhost:3000")
public class PnLController {

    private final PnLService pnlService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepo;

    public PnLController(
            PnLService pnlService,
            JwtUtils jwtUtils,
            UserRepository userRepo) {
        this.pnlService = pnlService;
        this.jwtUtils = jwtUtils;
        this.userRepo = userRepo;
    }

    @GetMapping
    public PnLSummaryDTO getPnL(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtils.extractUsername(token);

        User user = userRepo.findByEmail(email).orElseThrow();

        return pnlService.calculatePnL(user.getId());
    }
    @GetMapping("/timeline")
    public List<PnLTimelineDTO> getPnLTimeline(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtils.extractUsername(token);

        User user = userRepo.findByEmail(email).orElseThrow();

        return pnlService.calculatePnLTimeline(user.getId());
    }

}
