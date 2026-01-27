package com.CryptoProject.CryptoInfosys.dto;

import jakarta.validation.constraints.NotBlank;

public class AddExchangeAccountRequest {

    @NotBlank
    private String exchange;
    @NotBlank
    private String apiKey;
    @NotBlank
    private String apiSecret;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }
}
