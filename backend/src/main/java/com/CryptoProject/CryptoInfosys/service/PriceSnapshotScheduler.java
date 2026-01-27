package com.CryptoProject.CryptoInfosys.service;

import com.CryptoProject.CryptoInfosys.model.PriceSnapshot;
import com.CryptoProject.CryptoInfosys.repository.PriceSnapshotRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PriceSnapshotScheduler {

    private final MarketPriceService marketPriceService;
    private final PriceSnapshotRepository priceSnapshotRepository;

    public PriceSnapshotScheduler(
            MarketPriceService marketPriceService,
            PriceSnapshotRepository priceSnapshotRepository
    ) {
        this.marketPriceService = marketPriceService;
        this.priceSnapshotRepository = priceSnapshotRepository;
    }

    // ⏱ Runs every 5 minutes
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void capturePriceSnapshots() {

        List<Map<String, Object>> prices =
                marketPriceService.fetchMarketPrices();

        LocalDateTime now = LocalDateTime.now();

        for (Map<String, Object> coin : prices) {

            String symbol = coin.get("symbol").toString().toUpperCase();
            Double price = Double.valueOf(
                    coin.get("current_price").toString()
            );

            PriceSnapshot snapshot =
                    new PriceSnapshot(symbol, price, now);

            priceSnapshotRepository.save(snapshot);
        }

        System.out.println("✅ Price snapshots saved at " + now);
    }
    
}
