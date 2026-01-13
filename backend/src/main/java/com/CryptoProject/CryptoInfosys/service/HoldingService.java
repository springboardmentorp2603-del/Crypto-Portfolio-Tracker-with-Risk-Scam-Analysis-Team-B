package com.CryptoProject.CryptoInfosys.service;

import com.CryptoProject.CryptoInfosys.model.Holding;
import com.CryptoProject.CryptoInfosys.model.User;
import com.CryptoProject.CryptoInfosys.repository.HoldingRepository;
import com.CryptoProject.CryptoInfosys.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HoldingService {

    private final HoldingRepository holdingRepo;
    private final UserRepository userRepo;
    private final PricingService pricingService;

    public HoldingService(
            HoldingRepository holdingRepo,
            UserRepository userRepo,
            PricingService pricingService
    ) {
        this.holdingRepo = holdingRepo;
        this.userRepo = userRepo;
        this.pricingService = pricingService;
    }

    // 🔵 EXISTING (DO NOT TOUCH – USED BY HOLDINGS PAGE)
    public List<Holding> getHoldings(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        return holdingRepo.findByUser(user);
    }

    // =========================================================
    // 🔥 DASHBOARD METHODS (CORRECT & SAFE)
    // =========================================================

    // ✅ Active holdings count
    public int getActiveHoldingsCount(String email) {
        return (int) getHoldings(email)
                .stream()
                .filter(h -> h.getQuantity().doubleValue() > 0)
                .count();
    }


    // ✅ Total portfolio value using LIVE prices
    public double getTotalPortfolioValue(String email) {

        List<Holding> holdings = getHoldings(email);

        // Build price map from PricingService
        Map<String, Double> priceMap =
                pricingService.getPrices()
                        .stream()
                        .collect(Collectors.toMap(
                                p -> p.symbol,
                                p -> p.priceUsd
                        ));

        return holdings.stream()
                .mapToDouble(h -> {
                    double price =
                            priceMap.getOrDefault(
                                    h.getSymbol(),
                                    0.0
                            );
                    return price * h.getQuantity().doubleValue();
                })
                .sum();
    }
}
