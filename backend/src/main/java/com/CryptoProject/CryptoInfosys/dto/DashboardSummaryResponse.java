package com.CryptoProject.CryptoInfosys.dto;

public class DashboardSummaryResponse {

    private double totalPortfolioValue;
    private double realizedPnL;
    private double unrealizedPnL;
    private int activeHoldings;

    public DashboardSummaryResponse(
            double totalPortfolioValue,
            double realizedPnL,
            double unrealizedPnL,
            int activeHoldings
    ) {
        this.totalPortfolioValue = totalPortfolioValue;
        this.realizedPnL = realizedPnL;
        this.unrealizedPnL = unrealizedPnL;
        this.activeHoldings = activeHoldings;
    }

    public double getTotalPortfolioValue() {
        return totalPortfolioValue;
    }

    public double getRealizedPnL() {
        return realizedPnL;
    }

    public double getUnrealizedPnL() {
        return unrealizedPnL;
    }

    public int getActiveHoldings() {
        return activeHoldings;
    }
}
