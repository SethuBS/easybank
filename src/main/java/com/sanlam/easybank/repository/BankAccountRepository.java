package com.sanlam.easybank.repository;

import com.sanlam.easybank.model.BankAccount;

import java.math.BigDecimal;
import java.util.Optional;

public interface BankAccountRepository {
    BigDecimal getBalance(Long accountId);

    int updateBalance(Long accountId, BigDecimal amount);

    void insertAccount(BankAccount bankAccount);

    Optional<BankAccount> findById(Long accountId);
}
