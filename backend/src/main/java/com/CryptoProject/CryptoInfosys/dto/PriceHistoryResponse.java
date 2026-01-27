package com.CryptoProject.CryptoInfosys.dto;

import java.time.LocalDateTime;

public class PriceHistoryResponse {

    private Double price;
    private LocalDateTime time;

    public PriceHistoryResponse(Double price, LocalDateTime time) {
        this.price = price;
        this.time = time;
    }

    public Double getPrice() {
        return price;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
