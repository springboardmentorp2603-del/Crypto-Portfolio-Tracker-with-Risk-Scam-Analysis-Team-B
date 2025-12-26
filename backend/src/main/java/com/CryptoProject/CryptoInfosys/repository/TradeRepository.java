package com.CryptoProject.CryptoInfosys.repository;

import com.CryptoProject.CryptoInfosys.model.Trade;
import com.CryptoProject.CryptoInfosys.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

    List<Trade> findByUser(User user);
    List<Trade> findByUserId(Long userId);
    List<Trade> findByUserIdOrderByExecutedAtAsc(Long userId);
    List<Trade> findByUserOrderByExecutedAtAsc(User user);

}
