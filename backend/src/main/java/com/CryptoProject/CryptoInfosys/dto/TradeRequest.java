package com.CryptoProject.CryptoInfosys.dto;

import com.CryptoProject.CryptoInfosys.model.TradeSide;
import java.math.BigDecimal;

public class TradeRequest {

    private String assetSymbol;
    private String exchange;
    private TradeSide side;   // ✅ ENUM, not String
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal fee;

    // getters & setters
    public String getAssetSymbol() { return assetSymbol; }
    public void setAssetSymbol(String assetSymbol) { this.assetSymbol = assetSymbol; }

    public TradeSide getSide() { return side; }
    public void setSide(TradeSide side) { this.side = side; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }

    public String getExchange() {
        return exchange;
    }
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}
