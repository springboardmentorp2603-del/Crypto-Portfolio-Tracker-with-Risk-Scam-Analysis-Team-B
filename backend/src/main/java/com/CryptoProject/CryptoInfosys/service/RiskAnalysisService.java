package com.CryptoProject.CryptoInfosys.service;

import com.CryptoProject.CryptoInfosys.dto.PricingDTO;
import com.CryptoProject.CryptoInfosys.dto.RiskAlertDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RiskAnalysisService {

    public List<RiskAlertDTO> analyze(List<PricingDTO> prices) {

        List<RiskAlertDTO> alerts = new ArrayList<>();

        for (PricingDTO p : prices) {

            RiskAlertDTO alert = new RiskAlertDTO();
            alert.asset = p.name;
            alert.symbol = p.symbol;

            // 🔴 HIGH RISK
            if (Math.abs(p.change24h) > 15) {
                alert.riskLevel = "HIGH";
                alert.reason = "Extreme 24h price volatility";
            }
            // 🟡 MEDIUM RISK
            else if (p.marketCap < 10000000000L) { // < ₹1,000 Cr
                alert.riskLevel = "MEDIUM";
                alert.reason = "Low market capitalization";
            }
            // 🟢 LOW RISK
            else {
                alert.riskLevel = "LOW";
                alert.reason = "Stable market conditions";
            }

            alerts.add(alert);
        }

        return alerts;
    }
}
