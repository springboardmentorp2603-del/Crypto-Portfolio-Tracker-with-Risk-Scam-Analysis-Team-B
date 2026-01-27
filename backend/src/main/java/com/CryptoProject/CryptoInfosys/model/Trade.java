package com.CryptoProject.CryptoInfosys.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.CryptoProject.CryptoInfosys.model.User;;
import com.CryptoProject.CryptoInfosys.model.TradeSide;


@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private String assetSymbol;
    private String exchange;
    private String assetType;

    @Enumerated(EnumType.STRING)
    private TradeSide side;

    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal fee;

    private LocalDateTime executedAt;

    // ✅ REQUIRED GETTERS & SETTERS
    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getAssetSymbol() { return assetSymbol; }
    public void setAssetSymbol(String assetSymbol) {
        this.assetSymbol = assetSymbol;
    }

    public String getExchange() { return exchange; }
    public void setExchange(String exchange) { this.exchange = exchange; }
    public String getAssetType() { return assetType; }
    public void setAssetType(String assetType) { this.assetType = assetType; }

    public TradeSide getSide() { return side; }
    public void setSide(TradeSide side) { this.side = side; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }

    public LocalDateTime getExecutedAt() { return executedAt; }
    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }
}
