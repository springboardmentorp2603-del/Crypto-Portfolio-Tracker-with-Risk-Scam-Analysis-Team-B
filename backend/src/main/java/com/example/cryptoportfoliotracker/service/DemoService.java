package com.example.cryptoportfoliotracker.service;

import org.springframework.stereotype.Service;

@Service
public class DemoService {

    private boolean demoMode = false;

    public void setDemoMode(boolean demoMode) {
        this.demoMode = demoMode;
    }

    public boolean isDemoMode() {
        return demoMode;
    }

    // Mock data methods (integrate into existing controllers/services as needed)
    public Object getMockPortfolio() {
        return "{ \"holdings\": [{\"crypto\": \"BTC\", \"amount\": 1.0}], \"pnl\": 1000.0 }";
    }
}
