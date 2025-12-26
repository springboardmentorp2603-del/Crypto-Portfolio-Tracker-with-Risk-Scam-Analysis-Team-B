package com.CryptoProject.CryptoInfosys.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TradeDTO {
    public Long id;
    public String assetSymbol;
    public String side;
    public BigDecimal quantity;
    public BigDecimal price;
    public BigDecimal fee;
    public LocalDateTime executedAt;
}
