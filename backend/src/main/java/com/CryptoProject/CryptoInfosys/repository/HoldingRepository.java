package com.CryptoProject.CryptoInfosys.repository;

import com.CryptoProject.CryptoInfosys.model.Holding;
import com.CryptoProject.CryptoInfosys.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoldingRepository extends JpaRepository<Holding, Integer> {

    List<Holding> findByUser(User user);

    Optional<Holding> findByUserAndSymbolAndExchange(
            User user,
            String symbol,
            String exchange
    );

    List<Holding> findByUser_Email(String email);

    void deleteByUser(User user);
}
