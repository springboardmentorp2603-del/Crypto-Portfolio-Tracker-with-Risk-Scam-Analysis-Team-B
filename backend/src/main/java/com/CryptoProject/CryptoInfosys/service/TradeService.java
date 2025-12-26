package com.CryptoProject.CryptoInfosys.service;

import com.CryptoProject.CryptoInfosys.dto.TradeRequest;
import com.CryptoProject.CryptoInfosys.model.Holding;
import com.CryptoProject.CryptoInfosys.model.Trade;
import com.CryptoProject.CryptoInfosys.model.User;
import com.CryptoProject.CryptoInfosys.repository.HoldingRepository;
import com.CryptoProject.CryptoInfosys.repository.TradeRepository;
import com.CryptoProject.CryptoInfosys.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TradeService {

    private final TradeRepository tradeRepo;
    private final UserRepository userRepo;
    private final HoldingRepository holdingRepo;

    public TradeService(
            TradeRepository tradeRepo,
            UserRepository userRepo,
            HoldingRepository holdingRepo
    ) {
        this.tradeRepo = tradeRepo;
        this.userRepo = userRepo;
        this.holdingRepo = holdingRepo;
    }

    /* ================= ADD TRADE ================= */
    @Transactional
    public Trade addTrade(TradeRequest request, String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Trade trade = new Trade();
        trade.setUser(user);
        trade.setAssetSymbol(request.getAssetSymbol());
        trade.setSide(request.getSide());
        trade.setQuantity(request.getQuantity());
        trade.setPrice(request.getPrice());
        trade.setFee(request.getFee());
        trade.setExecutedAt(LocalDateTime.now());

        Trade saved = tradeRepo.save(trade);

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

        trade.setAssetSymbol(request.getAssetSymbol());
        trade.setSide(request.getSide());
        trade.setQuantity(request.getQuantity());
        trade.setPrice(request.getPrice());
        trade.setFee(request.getFee());

        Trade saved = tradeRepo.save(trade);

        rebuildHoldingsForUser(user);

        return saved;
    }

    /* ================= DELETE TRADE ================= */
    @Transactional
    public void deleteTrade(Long id, String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Trade trade = tradeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Trade not found"));

        if (!trade.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized delete");
        }

        tradeRepo.delete(trade);

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

        // 2️⃣ Get all trades in correct order
        List<Trade> trades =
                tradeRepo.findByUserOrderByExecutedAtAsc(user);

        Map<String, Holding> holdingsMap = new HashMap<>();

        for (Trade t : trades) {

            String symbol = t.getAssetSymbol();

            holdingsMap.putIfAbsent(symbol, new Holding(
                    null,
                    user,
                    symbol,
                    symbol,
                    0.0,
                    0.0
            ));

            Holding h = holdingsMap.get(symbol);

            double qty = h.getQuantity();
            double price = h.getPrice();
            double tradeQty = t.getQuantity().doubleValue();
            double tradePrice = t.getPrice().doubleValue();

            /* -------- BUY -------- */
            if (t.getSide().name().equals("BUY")) {

                double totalCost = (qty * price) + (tradeQty * tradePrice);
                double newQty = qty + tradeQty;

                h.setQuantity(newQty);
                h.setPrice(totalCost / newQty);
            }

            /* -------- SELL -------- */
            if (t.getSide().name().equals("SELL")) {

                if (tradeQty > qty) {
                    throw new IllegalStateException(
                            "Invalid SELL detected during rebuild. Symbol: "
                                    + symbol + ", Available: " + qty
                    );
                }

                h.setQuantity(qty - tradeQty);
            }
        }

        // 3️⃣ Save only non-zero holdings
        holdingsMap.values().removeIf(h -> h.getQuantity() <= 0);

        holdingRepo.saveAll(holdingsMap.values());
    }
}
