package com.CryptoProject.CryptoInfosys.service;

import com.CryptoProject.CryptoInfosys.dto.PricingDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PricingService {

    private static final String COINGECKO_URL =
            "https://api.coingecko.com/api/v3/coins/markets" +
            		"?vs_currency=inr" +
            "&order=market_cap_desc" +
            "&per_page=20" +
            "&page=1" +
            "&sparkline=true";

    public List<PricingDTO> getPrices() {

        RestTemplate restTemplate = new RestTemplate();

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> response =
                restTemplate.getForObject(COINGECKO_URL, List.class);

        List<PricingDTO> prices = new ArrayList<>();

        if (response == null) {
            return prices;
        }

        for (Map<String, Object> coin : response) {

            PricingDTO dto = new PricingDTO();

            dto.id = (String) coin.get("id");
            dto.symbol = ((String) coin.get("symbol")).toUpperCase();
            dto.name = (String) coin.get("name");

            dto.priceUsd = coin.get("current_price") != null
                    ? ((Number) coin.get("current_price")).doubleValue()
                    : 0;

            dto.change24h = coin.get("price_change_percentage_24h") != null
                    ? ((Number) coin.get("price_change_percentage_24h")).doubleValue()
                    : 0;

            dto.marketCap = coin.get("market_cap") != null
                    ? ((Number) coin.get("market_cap")).doubleValue()
                    : 0;

            // 🔥 SAFE SPARKLINE PARSING (THIS FIXES YOUR ISSUE)
            Object sparklineObj = coin.get("sparkline_in_7d");

            if (sparklineObj instanceof Map<?, ?> sparklineMap) {
                Object pricesObj = sparklineMap.get("price");

                if (pricesObj instanceof List<?> priceList) {
                    List<Double> sparkline = new ArrayList<>();

                    for (Object p : priceList) {
                        if (p instanceof Number) {
                            sparkline.add(((Number) p).doubleValue());
                        }
                    }

                    dto.sparkline = sparkline;
                }
            }

            prices.add(dto);
        }

        return prices;
    }
}
