package com.sanlam.easybank.repository;

import com.sanlam.easybank.model.BankAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class BankAccountRepository {

    private static final Logger logger = LoggerFactory.getLogger(BankAccountRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public BankAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal getBalance(Long accountId) {
        logger.debug("Fetching balance for account {}", accountId);
        String sql = "SELECT balance FROM accounts WHERE id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
        logger.debug("Balance for account {}: {}", accountId, balance);
        return balance;
    }

    public int updateBalance(Long accountId, BigDecimal amount) {
        logger.debug("Updating balance for account {} by deducting amount {}", accountId, amount);
        String sql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, amount, accountId);
        logger.debug("Balance updated for account {}. Rows affected: {}", accountId, rowsAffected);
        return rowsAffected;
    }

    public void insertAccount(BankAccount bankAccount) {
        logger.debug("Inserting new account: {}", bankAccount);
        String sql = "INSERT INTO accounts (account_number, account_holder_name, balance, created_at) VALUES (?, ?, ?, ?)";
        int rowsInserted = jdbcTemplate.update(sql, bankAccount.getAccountNumber(), bankAccount.getAccountHolderName(), bankAccount.getBalance(), bankAccount.getCreatedAt());
        logger.debug("Account inserted. Rows affected: {}", rowsInserted);
    }
}
