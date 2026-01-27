package com.CryptoProject.CryptoInfosys.service;

import com.CryptoProject.CryptoInfosys.dto.TaxSummaryDTO;

public interface TaxService {
    TaxSummaryDTO calculateTaxSummary(Long userId);
}
