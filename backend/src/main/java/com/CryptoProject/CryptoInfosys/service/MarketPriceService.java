package com.CryptoProject.CryptoInfosys.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class MarketPriceService {

    private final RestTemplate restTemplate = new RestTemplate();

    // You can later make this dynamic (from holdings)
    private static final List<String> ASSETS = List.of(
    		  "bitcoin",
    		  "ethereum",
    		  "solana",
    		  "binancecoin",
    		  "ripple",
    		  "tether",
    		  "usd-coin",
    		  "tron"
    		);


    public List<Map<String, Object>> fetchMarketPrices() {

        String ids = String.join(",", ASSETS);

        String url =
            "https://api.coingecko.com/api/v3/coins/markets" +
            "?vs_currency=usd&ids=" + ids;

        return restTemplate.getForObject(url, List.class);
    }
}
