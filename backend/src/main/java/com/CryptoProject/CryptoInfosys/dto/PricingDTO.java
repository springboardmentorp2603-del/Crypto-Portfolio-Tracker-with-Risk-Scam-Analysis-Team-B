package com.CryptoProject.CryptoInfosys.dto;

import java.util.List;

public class PricingDTO {
    public String id;
    public String symbol;
    public String name;
    public double priceUsd;
    public double change24h;
    public double marketCap;
    public List<Double> sparkline;
}
