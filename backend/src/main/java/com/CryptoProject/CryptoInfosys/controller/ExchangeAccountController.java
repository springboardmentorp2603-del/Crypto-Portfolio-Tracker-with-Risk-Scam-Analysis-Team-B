package com.CryptoProject.CryptoInfosys.controller;

import com.CryptoProject.CryptoInfosys.model.ExchangeAccount;
import com.CryptoProject.CryptoInfosys.dto.AddExchangeAccountRequest;
import com.CryptoProject.CryptoInfosys.service.ExchangeAccountService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.CryptoProject.CryptoInfosys.repository.ExchangeAccountRepository;
import com.CryptoProject.CryptoInfosys.service.BinanceService;
import jakarta.validation.Valid;
import com.CryptoProject.CryptoInfosys.service.ExchangeAccountService;

import java.util.List;

@Valid
@RestController
@RequestMapping("/api/exchange-accounts")
@CrossOrigin(origins = "http://localhost:3000")
public class ExchangeAccountController {

    private final ExchangeAccountRepository exchangeRepo;
    private final BinanceService binanceService;
    private final ExchangeAccountService exchangeAccountService;


    public ExchangeAccountController(
            ExchangeAccountRepository exchangeRepo,
            BinanceService binanceService,
            ExchangeAccountService exchangeAccountService
    ) {
        this.exchangeRepo = exchangeRepo;
        this.binanceService = binanceService;
        this.exchangeAccountService = exchangeAccountService;
    }


    @PostMapping
    public ExchangeAccount addExchange(
            @Valid @RequestBody AddExchangeAccountRequest request,
            Authentication auth
    ) {
        return exchangeAccountService.addExchange(
                auth.getName(),
                request.getExchange(),
                request.getApiKey(),
                request.getApiSecret()
        );
    }

    // @GetMapping("/sync/binance")
    // public String syncBinance(Authentication auth) {

    //     ExchangeAccount acc = exchangeRepo
    //             .findByUserEmailAndExchange(auth.getName(), "BINANCE")
    //             .orElseThrow(() -> new RuntimeException("Exchange not found"));

    //     return binanceService.getAccountInfo(
    //             decrypt(acc.getApiKey()),
    //             decrypt(acc.getApiSecret())
    //     );
    // }

    @GetMapping("/sync/binance")
public String syncBinance(Authentication auth) {

    try {
        System.out.println("🔹 Sync Binance called by: " + auth.getName());

        ExchangeAccount acc = exchangeRepo
                .findByUserEmailAndExchange(auth.getName(), "BINANCE")
                .orElseThrow(() -> new RuntimeException("Exchange not found"));

        System.out.println("🔹 Exchange found");

        String apiKey = decrypt(acc.getApiKey());
        String apiSecret = decrypt(acc.getApiSecret());

        System.out.println("🔹 Keys decrypted");

        return binanceService.getAccountInfo(apiKey, apiSecret);

    } catch (Exception e) {
        e.printStackTrace(); // 👈 THIS IS CRITICAL
        throw e;
    }
}

@DeleteMapping("/{exchange}")
public void disconnectExchange(
        @PathVariable String exchange,
        Authentication auth
) {
    exchangeAccountService.disconnectExchange(
        auth.getName(),
        exchange.toUpperCase()
    );
}



    private String decrypt(String encrypted) {
        return encrypted; // TEMP (will wire AES later if needed)
    }
}
