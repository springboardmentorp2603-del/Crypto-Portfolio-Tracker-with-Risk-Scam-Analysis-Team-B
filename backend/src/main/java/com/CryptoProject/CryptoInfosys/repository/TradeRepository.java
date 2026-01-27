package com.CryptoProject.CryptoInfosys.repository;

import com.CryptoProject.CryptoInfosys.model.Trade;
import com.CryptoProject.CryptoInfosys.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    // Used in PnLService.calculateAssetPnL()
    List<Trade> findByUserId(Long userId);

    // Used in calculatePnLTimeline()
    List<Trade> findByUserIdOrderByExecutedAtAsc(Long userId);

    // Used in rebuildHoldingsForUser()
    List<Trade> findByUser(User user);

    // Used in deleteTrade()
    Optional<Trade> findByIdAndUser(Long id, User user);

    // Used in Profit/Loss alert
    @Query("SELECT t.user.id FROM Trade t WHERE t.user.email = :email")
    Long findUserIdByEmail(String email);
}
