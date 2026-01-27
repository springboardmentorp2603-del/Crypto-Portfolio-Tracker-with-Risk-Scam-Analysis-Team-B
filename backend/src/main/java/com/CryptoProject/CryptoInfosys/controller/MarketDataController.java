package com.CryptoProject.CryptoInfosys.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/market")
@CrossOrigin
public class MarketDataController {

    private final RestTemplate restTemplate = new RestTemplate();

    // Simple in-memory cache
    private Map<String, Object> cache = new HashMap<>();
    private long lastFetchTime = 0;

    private static final long CACHE_DURATION = 5 * 60 * 1000; // 5 minutes

    @GetMapping(value = "/coins", produces="application/json")
    public Object getMarketCoins() {

        long now = System.currentTimeMillis();

        // ✅ Return cached data if valid
        if (cache.containsKey("coins") && (now - lastFetchTime) < CACHE_DURATION) {
            return cache.get("coins");
        }

        // 🔥 CoinGecko API (called rarely)
        String url =
            "https://api.coingecko.com/api/v3/coins/markets" +
            "?vs_currency=usd&order=market_cap_desc&per_page=50&page=1";

        Object data = restTemplate.getForObject(url, Object.class);

        // Save to cache
        cache.put("coins", data);
        lastFetchTime = now;

        return data;
    }
}
