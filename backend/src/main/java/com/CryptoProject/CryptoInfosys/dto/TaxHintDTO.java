package com.CryptoProject.CryptoInfosys.dto;



public record TaxHintDTO(
        String symbol,
        double realizedGain,
        double estimatedTax,
        String holdingPeriod,
        long daysHeld,
        String hintType,
        String hint
) {}
