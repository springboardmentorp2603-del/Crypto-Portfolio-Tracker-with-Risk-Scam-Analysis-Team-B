package com.CryptoProject.CryptoInfosys.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_snapshots")
public class PriceSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String assetSymbol;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private LocalDateTime capturedAt;

    public PriceSnapshot() {
    }

    public PriceSnapshot(String assetSymbol, Double price, LocalDateTime capturedAt) {
        this.assetSymbol = assetSymbol;
        this.price = price;
        this.capturedAt = capturedAt;
    }

    // ---------- Getters & Setters ----------

    public Long getId() {
        return id;
    }

    public String getAssetSymbol() {
        return assetSymbol;
    }

    public void setAssetSymbol(String assetSymbol) {
        this.assetSymbol = assetSymbol;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getCapturedAt() {
        return capturedAt;
    }

    public void setCapturedAt(LocalDateTime capturedAt) {
        this.capturedAt = capturedAt;
    }
}
