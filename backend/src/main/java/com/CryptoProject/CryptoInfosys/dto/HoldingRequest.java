package com.CryptoProject.CryptoInfosys.dto;

public class HoldingRequest {
    private String asset;
    private String symbol;
    private Double quantity;
    private Double price;

    public String getAsset() { return asset; }
    public void setAsset(String asset) { this.asset = asset; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
