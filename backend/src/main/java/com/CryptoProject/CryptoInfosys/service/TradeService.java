package com.CryptoProject.CryptoInfosys.service;

import com.CryptoProject.CryptoInfosys.dto.TradeRequest;
import com.CryptoProject.CryptoInfosys.model.Holding;
import com.CryptoProject.CryptoInfosys.model.Trade;
import com.CryptoProject.CryptoInfosys.model.User;
import com.CryptoProject.CryptoInfosys.repository.HoldingRepository;
import com.CryptoProject.CryptoInfosys.repository.TradeRepository;
import com.CryptoProject.CryptoInfosys.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.CryptoProject.CryptoInfosys.model.TradeSide;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TradeService {

    private final TradeRepository tradeRepo;
    private final UserRepository userRepo;
    private final HoldingRepository holdingRepo;
    private final NotificationService notificationService;

    public TradeService(
            TradeRepository tradeRepo,
            UserRepository userRepo,
            HoldingRepository holdingRepo,
            NotificationService notificationService
    ) {
        this.tradeRepo = tradeRepo;
        this.userRepo = userRepo;
        this.holdingRepo = holdingRepo;
        this.notificationService = notificationService;
    }

    /* ================= ADD TRADE ================= */
    @Transactional
public Trade addTrade(TradeRequest request, String email) {

    User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (request.getAssetSymbol() == null || request.getAssetSymbol().isBlank()) {
        throw new IllegalArgumentException("Asset symbol is required");
    }
    if (request.getExchange() == null || request.getExchange().isBlank()) {
        throw new IllegalArgumentException("Exchange is required");
    }

    Trade trade = new Trade();
    trade.setUser(user);
    trade.setAssetSymbol(request.getAssetSymbol());
    trade.setExchange(request.getExchange());          
    trade.setSide(request.getSide());
    trade.setQuantity(request.getQuantity());
    trade.setPrice(request.getPrice());
    trade.setFee(request.getFee());
    trade.setExecutedAt(LocalDateTime.now());

    Trade saved = tradeRepo.save(trade);

    notificationService.createNotification(
            email,
            "Trade Executed",
            request.getSide() + " " +
                    request.getQuantity() + " " +
                    request.getAssetSymbol() +
                    " on " + request.getExchange() +
                    " @ ₹" + request.getPrice(),
            "INFO"
    );

    rebuildHoldingsForUser(user);
    return saved;
}


    /* ================= UPDATE TRADE ================= */
    @Transactional
public Trade updateTrade(Long id, TradeRequest request, String email) {

    User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Trade trade = tradeRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Trade not found"));

    if (!trade.getUser().getId().equals(user.getId())) {
        throw new RuntimeException("Unauthorized update");
    }

    if (request.getExchange() == null || request.getExchange().isBlank()) {
        throw new IllegalArgumentException("Exchange is required");
    }

    BigDecimal oldQty = trade.getQuantity();
    BigDecimal oldPrice = trade.getPrice();

    trade.setAssetSymbol(request.getAssetSymbol());
    trade.setExchange(request.getExchange());          
    trade.setSide(request.getSide());
    trade.setQuantity(request.getQuantity());
    trade.setPrice(request.getPrice());
    trade.setFee(request.getFee());

    Trade saved = tradeRepo.save(trade);

    notificationService.createNotification(
            email,
            "Trade Updated",
            trade.getAssetSymbol() + " (" + trade.getExchange() + ")" +
                    " updated (Qty: " + oldQty + " → " + request.getQuantity() +
                    ", Price: ₹" + oldPrice + " → ₹" + request.getPrice() + ")",
            "INFO"
    );

    rebuildHoldingsForUser(user);
    return saved;
}


    /* ================= DELETE TRADE ================= */
    @Transactional
public void deleteTrade(Long id, String email) {

    User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Trade trade = tradeRepo.findByIdAndUser(id, user)
            .orElseThrow(() -> new RuntimeException("Trade not found"));

    tradeRepo.delete(trade);

    notificationService.createNotification(
            email,
            "Trade Deleted",
            trade.getAssetSymbol() +
                    " (" + trade.getExchange() + ")" +
                    " trade deleted (Qty: " + trade.getQuantity() +
                    ", Price: ₹" + trade.getPrice() + ")",
            "INFO"
    );

    rebuildHoldingsForUser(user);
}


    /* ================= GET TRADES ================= */
    @Transactional(readOnly = true)
    public List<Trade> getTrades(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        return tradeRepo.findByUser(user);
    }

    /* ================= REBUILD HOLDINGS (CORE LOGIC) ================= */
    @Transactional
public void rebuildHoldingsForUser(User user) {

    // 1️⃣ Clear existing holdings
    holdingRepo.deleteByUser(user);

    // 2️⃣ Fetch all trades
    List<Trade> trades = tradeRepo.findByUser(user);

    // 3️⃣ Rebuild holdings from trades
    for (Trade t : trades) {

        Holding holding = holdingRepo
                .findByUserAndSymbolAndExchange(
                        user,
                        t.getAssetSymbol(),
                        t.getExchange()
                )
                .orElseGet(() -> {
                    Holding h = new Holding();
                    h.setUser(user);
                    h.setSymbol(t.getAssetSymbol());
                    h.setAsset(t.getAssetSymbol());
                    h.setExchange(t.getExchange());
                    h.setQuantity(BigDecimal.ZERO);
                    h.setPrice(t.getPrice());
                    return h;
                });

        // 4️⃣ Update quantity based on BUY / SELL
        if (t.getSide() == TradeSide.BUY) {
            holding.setQuantity(
                    holding.getQuantity().add(t.getQuantity())
            );
        } else if (t.getSide() == TradeSide.SELL) {
            holding.setQuantity(
                    holding.getQuantity().subtract(t.getQuantity())
            );
        }

        // 5️⃣ Update latest price (optional but recommended)
        holding.setPrice(t.getPrice());

        // 6️⃣ Save
        holdingRepo.save(holding);
    }
}

}
