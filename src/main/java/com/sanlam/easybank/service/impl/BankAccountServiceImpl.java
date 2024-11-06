package com.sanlam.easybank.service.impl;

import com.sanlam.easybank.exception.InsufficientFundsException;
import com.sanlam.easybank.model.WithdrawalEvent;
import com.sanlam.easybank.publisher.EventPublisher;
import com.sanlam.easybank.repository.BankAccountRepository;
import com.sanlam.easybank.service.BankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private static final Logger logger = LoggerFactory.getLogger(BankAccountServiceImpl.class);

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
        logger.info("Attempting to withdraw amount {} from account {}", amount, accountId);

        BigDecimal currentBalance = bankAccountRepository.getBalance(accountId);
        if (currentBalance.compareTo(amount) < 0) {
            logger.warn("Insufficient funds for account {}. Current balance: {}, Withdrawal amount: {}", accountId, currentBalance, amount);
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }

        bankAccountRepository.updateBalance(accountId, amount);
        logger.info("Withdrawal successful for account {}. Amount: {}. New balance: {}", accountId, amount, currentBalance.subtract(amount));

        // Publish event after successful withdrawal
        WithdrawalEvent event = new WithdrawalEvent(amount, accountId, "SUCCESSFUL");
        eventPublisher.publish(event);
        logger.info("Withdrawal event published for account {}: {}", accountId, event);
    }

    @Override
    public BigDecimal getBalance(Long accountId) {
        return bankAccountRepository.getBalance(accountId);
    }
}
