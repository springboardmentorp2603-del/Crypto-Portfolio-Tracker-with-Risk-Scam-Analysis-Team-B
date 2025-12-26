package com.CryptoProject.CryptoInfosys.repository;

import com.CryptoProject.CryptoInfosys.model.Holding;
import com.CryptoProject.CryptoInfosys.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoldingRepository extends JpaRepository<Holding, Integer> {
    List<Holding> findByUser(User user);
    Holding findByUserAndSymbol(User user, String symbol);
    void deleteByUser(User user);
}
