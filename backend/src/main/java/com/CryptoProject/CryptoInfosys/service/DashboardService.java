package com.CryptoProject.CryptoInfosys.service;

import com.CryptoProject.CryptoInfosys.dto.DashboardSummaryResponse;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final HoldingService holdingService;
    private final PnLService pnlService;

    public DashboardService(
            HoldingService holdingService,
            PnLService pnlService
    ) {
        this.holdingService = holdingService;
        this.pnlService = pnlService;
    }

    /**
     * Dashboard summary for logged-in user
     */
    public DashboardSummaryResponse getSummaryByEmail(String email) {

        double totalPortfolioValue =
                holdingService.getTotalPortfolioValue(email);

        int activeHoldings =
                holdingService.getActiveHoldingsCount(email);

        double realizedPnL =
                pnlService.getTotalRealizedPnL(email);

        double unrealizedPnL =
                pnlService.getTotalUnrealizedPnL(email);

        return new DashboardSummaryResponse(
                totalPortfolioValue,
                realizedPnL,
                unrealizedPnL,
                activeHoldings
        );
    }
}
