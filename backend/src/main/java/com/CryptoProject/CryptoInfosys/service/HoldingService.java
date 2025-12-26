package com.CryptoProject.CryptoInfosys.service;

import com.CryptoProject.CryptoInfosys.model.Holding;
import com.CryptoProject.CryptoInfosys.model.Trade;
import com.CryptoProject.CryptoInfosys.model.User;
import com.CryptoProject.CryptoInfosys.repository.HoldingRepository;
import com.CryptoProject.CryptoInfosys.repository.TradeRepository;
import com.CryptoProject.CryptoInfosys.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HoldingService {

    private final HoldingRepository holdingRepo;
    private final UserRepository userRepo;
    private final TradeRepository tradeRepo;

    public HoldingService(HoldingRepository holdingRepo, UserRepository userRepo,TradeRepository tradeRepo) {
        this.holdingRepo = holdingRepo;
        this.userRepo = userRepo;
        this.tradeRepo = tradeRepo;
    }

    public List<Holding> getHoldings(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        return holdingRepo.findByUser(user);
    }
}

