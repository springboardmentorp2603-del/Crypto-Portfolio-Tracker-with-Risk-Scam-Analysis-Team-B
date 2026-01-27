package com.CryptoProject.CryptoInfosys.service;

import com.CryptoProject.CryptoInfosys.dto.TaxHintDTO;
import com.CryptoProject.CryptoInfosys.dto.TaxSummaryDTO;
import com.CryptoProject.CryptoInfosys.model.Trade;
import com.CryptoProject.CryptoInfosys.repository.TradeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.time.Duration;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.CryptoProject.CryptoInfosys.model.TradeSide;


@Service
public class TaxServiceImpl implements TaxService {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.30");

    private final TradeRepository tradeRepo;

    public TaxServiceImpl(TradeRepository tradeRepo) {
        this.tradeRepo = tradeRepo;
    }

    @Override
    public TaxSummaryDTO calculateTaxSummary(Long userId) {

        List<Trade> trades =
                tradeRepo.findByUserIdOrderByExecutedAtAsc(userId);

        Map<String, Deque<Trade>> buyLedger = new HashMap<>();

        BigDecimal totalRealized = BigDecimal.ZERO;
        BigDecimal shortTerm = BigDecimal.ZERO;
        BigDecimal longTerm = BigDecimal.ZERO;

        List<TaxHintDTO> hints = new ArrayList<>();

        for (Trade trade : trades) {

            String symbol = trade.getAssetSymbol();

            if (trade.getSide() == TradeSide.BUY) {
                buyLedger
                    .computeIfAbsent(symbol, k -> new ArrayDeque<>())
                    .add(trade);
                continue;
            }

            // SELL logic (FIFO)
            BigDecimal remainingQty = trade.getQuantity();

            while (remainingQty.compareTo(BigDecimal.ZERO) > 0 &&
                   buyLedger.containsKey(symbol) &&
                   !buyLedger.get(symbol).isEmpty()) {

                Trade buy = buyLedger.get(symbol).peek();

                BigDecimal matchedQty =
                        remainingQty.min(buy.getQuantity());

                BigDecimal buyCost =
                        buy.getPrice().multiply(matchedQty);

                BigDecimal sellValue =
                        trade.getPrice().multiply(matchedQty);

                BigDecimal gain =
                        sellValue.subtract(buyCost)
                        .subtract(
                            trade.getFee() != null
                                ? trade.getFee()
                                : BigDecimal.ZERO
                        );

                totalRealized = totalRealized.add(gain);

                long daysHeld = Duration.between(
                        buy.getExecutedAt(),
                        trade.getExecutedAt()
                ).toDays();

                boolean isLongTerm = daysHeld >= 365;

                if (isLongTerm) {
                    longTerm = longTerm.add(gain);
                } else {
                    shortTerm = shortTerm.add(gain);
                }

                BigDecimal tax =
                        gain.compareTo(BigDecimal.ZERO) > 0
                        ? gain.multiply(TAX_RATE)
                        : BigDecimal.ZERO;

                hints.add(new TaxHintDTO(
                        symbol,
                        gain.doubleValue(),
                        tax.doubleValue(),
                        isLongTerm ? "LONG_TERM" : "SHORT_TERM",
                        daysHeld,
                        isLongTerm ? "OPTIMIZATION" : "WARNING",
                        isLongTerm
                            ? "Eligible for long-term holding insight"
                            : "Short-term sale attracts full crypto tax"
                ));

                // Reduce quantities
                remainingQty = remainingQty.subtract(matchedQty);
                buy.setQuantity(buy.getQuantity().subtract(matchedQty));

                if (buy.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                    buyLedger.get(symbol).poll();
                }
            }
        }

        BigDecimal totalTax =
                totalRealized.compareTo(BigDecimal.ZERO) > 0
                ? totalRealized.multiply(TAX_RATE)
                : BigDecimal.ZERO;

        return new TaxSummaryDTO(
                round(totalRealized),
                round(totalTax),
                round(shortTerm),
                round(shortTerm.multiply(TAX_RATE)),
                round(longTerm),
                round(longTerm.multiply(TAX_RATE)),
                List.of(
                    "Crypto gains in India are taxed at a flat 30%",
                    "Losses cannot offset other income",
                    "FIFO accounting used for tax calculation"
                ),
                hints
        );
    }

    private double round(BigDecimal v) {
        return v.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
