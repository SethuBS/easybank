package com.sanlam.easybank.service.impl;

import com.sanlam.easybank.exception.InsufficientFundsException;
import com.sanlam.easybank.model.BankAccount;
import com.sanlam.easybank.model.WithdrawalEvent;
import com.sanlam.easybank.publisher.EventPublisher;
import com.sanlam.easybank.repository.BankAccountRepository;
import com.sanlam.easybank.repository.impl.BankAccountRepositoryImpl;
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
    public BankAccountServiceImpl(BankAccountRepositoryImpl bankAccountRepository, EventPublisher eventPublisher) {
        this.bankAccountRepository = bankAccountRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public void withdraw(Long accountId, BigDecimal amount) throws InsufficientFundsException {
        logger.info("Attempting to withdraw amount {} from account {}", amount, accountId);

        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));

        // Use domain logic to perform the withdrawal
        bankAccount.withdraw(amount);

        // Save updated account
        bankAccountRepository.updateBalance(accountId, amount);
        logger.info("Withdrawal successful for account {}. Amount: {}. New balance: {}", accountId, amount, bankAccount.getBalance());

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
