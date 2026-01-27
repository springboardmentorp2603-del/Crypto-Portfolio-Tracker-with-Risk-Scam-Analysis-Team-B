package com.CryptoProject.CryptoInfosys.model;

import jakarta.persistence.*;

@Entity
public class ExchangeAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private String exchange;

    @Column(length = 2048)
    private String apiKey;

    @Column(length = 2048)
    private String apiSecret;

    // Getters & setters
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getExchange() {
        return exchange;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }
}
