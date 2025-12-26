package com.CryptoProject.CryptoInfosys.controller;

import com.CryptoProject.CryptoInfosys.service.PricingService;
import com.CryptoProject.CryptoInfosys.service.RiskAnalysisService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/risk-alerts")
@CrossOrigin(origins = "http://localhost:3000")
public class RiskAlertController {

    private final PricingService pricingService;
    private final RiskAnalysisService riskService;

    public RiskAlertController(
            PricingService pricingService,
            RiskAnalysisService riskService) {
        this.pricingService = pricingService;
        this.riskService = riskService;
    }

    @GetMapping
    public Object getRiskAlerts() {
        return riskService.analyze(pricingService.getPrices());
    }
}
