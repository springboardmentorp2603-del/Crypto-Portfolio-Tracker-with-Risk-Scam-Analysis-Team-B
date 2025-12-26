package com.CryptoProject.CryptoInfosys.controller;

import com.CryptoProject.CryptoInfosys.dto.PricingDTO;
import com.CryptoProject.CryptoInfosys.service.PricingService;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pricing")
@CrossOrigin(origins = "http://localhost:3000")
public class PricingController {

    private final PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @GetMapping
    public List<PricingDTO> getPrices() {
        return pricingService.getPrices();
    }
}
