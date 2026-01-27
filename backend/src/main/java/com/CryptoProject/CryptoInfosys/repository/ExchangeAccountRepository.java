package com.CryptoProject.CryptoInfosys.repository;

import com.CryptoProject.CryptoInfosys.model.ExchangeAccount;
import com.CryptoProject.CryptoInfosys.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ExchangeAccountRepository extends JpaRepository<ExchangeAccount, Long> {

    List<ExchangeAccount> findByUser(User user);

    List<ExchangeAccount> findByUserEmail(String userEmail);

    boolean existsByUserAndExchange(User user, String exchange);

    void deleteByUserEmailAndExchange(String email, String exchange);


    Optional<ExchangeAccount> findByUserEmailAndExchange(String email, String exchange);
}
