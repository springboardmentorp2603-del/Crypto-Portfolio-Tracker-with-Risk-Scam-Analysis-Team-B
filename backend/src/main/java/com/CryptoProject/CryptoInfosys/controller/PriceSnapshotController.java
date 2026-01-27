package com.CryptoProject.CryptoInfosys.controller;

import com.CryptoProject.CryptoInfosys.dto.PriceHistoryResponse;
import com.CryptoProject.CryptoInfosys.service.PriceSnapshotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prices")
@CrossOrigin
public class PriceSnapshotController {

    private final PriceSnapshotService priceSnapshotService;

    public PriceSnapshotController(
            PriceSnapshotService priceSnapshotService
    ) {
        this.priceSnapshotService = priceSnapshotService;
    }

    @GetMapping
    public Map<String, List<PriceHistoryResponse>> getPrices() {
        return priceSnapshotService.getAllPriceHistory();
    }


    @GetMapping("/history/{asset}")
    public List<PriceHistoryResponse> getHistory(
            @PathVariable String asset,
            @RequestParam(defaultValue = "1D") String range
    ) {
        return priceSnapshotService.getPriceHistoryByRange(asset, range);
    }

    @GetMapping("/history")
    public Map<String, List<PriceHistoryResponse>> getAllHistory() {
        return priceSnapshotService.getAllPriceHistory();
    }
}
