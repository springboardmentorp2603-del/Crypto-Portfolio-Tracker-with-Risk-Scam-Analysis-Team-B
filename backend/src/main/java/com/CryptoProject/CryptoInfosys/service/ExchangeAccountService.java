package com.CryptoProject.CryptoInfosys.service;

import com.CryptoProject.CryptoInfosys.model.ExchangeAccount;
import com.CryptoProject.CryptoInfosys.model.User;
import com.CryptoProject.CryptoInfosys.repository.ExchangeAccountRepository;
import com.CryptoProject.CryptoInfosys.repository.UserRepository;
import com.CryptoProject.CryptoInfosys.security.AESUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeAccountService {

    private final ExchangeAccountRepository exchangeAccountRepo;
    private final UserRepository userRepo;

    @Value("${crypto.aes.secret}")
    private String aesSecret;

    // ✅ CONSTRUCTOR INJECTION
    public ExchangeAccountService(
            ExchangeAccountRepository exchangeAccountRepo,
            UserRepository userRepo
    ) {
        this.exchangeAccountRepo = exchangeAccountRepo;
        this.userRepo = userRepo;
    }

    // 🔐 Encryption helpers
    private String encrypt(String value) {
        return AESUtil.encrypt(value, aesSecret);
    }

    private String decrypt(String value) {
        return AESUtil.decrypt(value, aesSecret);
    }

    public List<ExchangeAccount> getUserExchanges(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return exchangeAccountRepo.findByUser(user);
    }

    public ExchangeAccount addExchange(
            String email,
            String exchange,
            String apiKey,
            String apiSecret
    ) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (exchangeAccountRepo.existsByUserAndExchange(user, exchange)) {
            throw new RuntimeException("Exchange already connected");
        }

        ExchangeAccount acc = new ExchangeAccount();
        acc.setUser(user);
        acc.setExchange(exchange);
        // encrypt the keys before storing
        acc.setApiKey(apiKey);
        acc.setApiSecret(apiSecret);

        return exchangeAccountRepo.save(acc);
    }

    public void disconnectExchange(String email, String exchange) {
        exchangeAccountRepo.deleteByUserEmailAndExchange(email, exchange);
    }

}
