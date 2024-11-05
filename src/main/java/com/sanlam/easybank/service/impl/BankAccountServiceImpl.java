package com.sanlam.easybank.service.impl;

import com.sanlam.easybank.exception.InsufficientFundsException;
import com.sanlam.easybank.model.WithdrawalEvent;
import com.sanlam.easybank.publisher.EventPublisher;
import com.sanlam.easybank.repository.BankAccountRepository;
import com.sanlam.easybank.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    private final EventPublisher eventPublisher;

    @Autowired
    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, EventPublisher eventPublisher) {
        this.bankAccountRepository = bankAccountRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public void withdraw(Long accountId, BigDecimal amount) throws InsufficientFundsException {
        BigDecimal currentBalance = bankAccountRepository.getBalance(accountId);
        if (currentBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }

        bankAccountRepository.updateBalance(accountId, amount);

        // Publish event after successful withdrawal
        eventPublisher.publish(new WithdrawalEvent(amount, accountId, "SUCCESSFUL"));
    }
}
