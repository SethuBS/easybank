package com.sanlam.easybank.service;

import com.sanlam.easybank.exception.InsufficientFundsException;

import java.math.BigDecimal;

public interface BankAccountService {
    void withdraw(Long accountId, BigDecimal amount) throws InsufficientFundsException;
    BigDecimal getBalance(Long accountId);
}
