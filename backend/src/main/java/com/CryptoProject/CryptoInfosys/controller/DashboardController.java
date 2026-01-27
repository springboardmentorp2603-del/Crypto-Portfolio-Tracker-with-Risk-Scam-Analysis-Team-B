package com.CryptoProject.CryptoInfosys.controller;

import com.CryptoProject.CryptoInfosys.dto.DashboardSummaryResponse;
import com.CryptoProject.CryptoInfosys.security.JwtUtils;
import com.CryptoProject.CryptoInfosys.service.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin
public class DashboardController {

    private final DashboardService dashboardService;
    private final JwtUtils jwtUtils;

    public DashboardController(
            DashboardService dashboardService,
            JwtUtils jwtUtils
    ) {
        this.dashboardService = dashboardService;
        this.jwtUtils = jwtUtils;
    }

    /**
     * GET /api/dashboard/summary
     * User is resolved from JWT
     */
    @GetMapping("/summary")
    public DashboardSummaryResponse getDashboardSummary(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);
        String email = jwtUtils.extractUsername(token);

        return dashboardService.getSummaryByEmail(email);
    }
}
