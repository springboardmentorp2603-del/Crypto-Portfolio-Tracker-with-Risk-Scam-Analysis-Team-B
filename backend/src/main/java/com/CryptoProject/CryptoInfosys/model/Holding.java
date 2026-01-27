package com.CryptoProject.CryptoInfosys.model;

import jakarta.persistence.*;

import com.CryptoProject.CryptoInfosys.model.User;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "holdings")
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Link to user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String asset;
    @Column(name = "asset_symbol", nullable = false)
    private String symbol;

    private String exchange;

    private BigDecimal quantity;
    private BigDecimal price;  // buy price or latest updated price

    public Holding() {}

    public Holding(Integer id, User user, String asset, String symbol, BigDecimal quantity, BigDecimal price, String username) {
        this.id = id;
        this.user = user;
        this.asset = asset;
        this.symbol = symbol;
        this.exchange = exchange;
        this.quantity = quantity;
        this.price = price;
    }
    public String getUserIdentifier() {
        return user != null ? user.getEmail() : null; // or getUserId()
    }

    @Override
    public String toString() {
        return "Holding{" +
                "id=" + id +
                ", user=" + (user != null ? user.getEmail() : "null") +
                ", asset='" + asset + '\'' +
                ", symbol='" + symbol + '\'' +
                ", exchange='" + exchange + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }

    @PrePersist
@PreUpdate
private void validate() {
    if (symbol == null || symbol.isBlank()) {
        throw new IllegalArgumentException("Asset symbol is required");
    }
    if (exchange == null || exchange.isBlank()) {
        throw new IllegalArgumentException("Exchange is required");
    }
    if (quantity == null || quantity.compareTo(BigDecimal.ZERO) < 0) {
        throw new IllegalArgumentException("Quantity cannot be negative");
    }
    if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
        throw new IllegalArgumentException("Price cannot be negative");
    }
}


    public String getSymbol() {
    return symbol;
}

public String getExchange() {
    return exchange;
}

public BigDecimal getQuantity() {
    return quantity;
}

public BigDecimal getPrice() {
    return price;
}

}
