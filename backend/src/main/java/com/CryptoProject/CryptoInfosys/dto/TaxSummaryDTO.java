package com.CryptoProject.CryptoInfosys.dto;

import java.util.List;

public record TaxSummaryDTO(
        double totalRealizedGains,
        double totalEstimatedTax,
        double shortTermGains,
        double shortTermTax,
        double longTermGains,
        double longTermTax,
        List<String> recommendations,
        List<TaxHintDTO> hints
) {}
