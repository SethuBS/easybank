package com.sanlam.easybank.repository.impl;

import com.sanlam.easybank.model.BankAccount;
import com.sanlam.easybank.repository.BankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public class BankAccountRepositoryImpl implements BankAccountRepository {

    private static final Logger logger = LoggerFactory.getLogger(BankAccountRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public BankAccountRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BigDecimal getBalance(Long accountId) {
        logger.debug("Fetching balance for account {}", accountId);
        String sql = "SELECT balance FROM accounts WHERE id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
        logger.debug("Balance for account {}: {}", accountId, balance);
        return balance;
    }

    @Override
    public int updateBalance(Long accountId, BigDecimal amount) {
        logger.debug("Updating balance for account {} by deducting amount {}", accountId, amount);
        String sql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, amount, accountId);
        logger.debug("Balance updated for account {}. Rows affected: {}", accountId, rowsAffected);
        return rowsAffected;
    }

    @Override
    public void insertAccount(BankAccount bankAccount) {
        logger.debug("Inserting new account: {}", bankAccount);
        String sql = "INSERT INTO accounts (account_number, account_holder_name, balance, created_at) VALUES (?, ?, ?, ?)";
        int rowsInserted = jdbcTemplate.update(sql, bankAccount.getAccountNumber(), bankAccount.getAccountHolderName(), bankAccount.getBalance(), bankAccount.getCreatedAt());
        logger.debug("Account inserted. Rows affected: {}", rowsInserted);
    }

    @Override
    public Optional<BankAccount> findById(Long accountId) {
        logger.debug("Finding account by ID: {}", accountId);
        String sql = "SELECT * FROM accounts WHERE id = ?";

        try {
            BankAccount account = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                BankAccount bankAccount = new BankAccount();
                bankAccount.setId(rs.getLong("id"));
                bankAccount.setAccountNumber(rs.getString("account_number"));
                bankAccount.setAccountHolderName(rs.getString("account_holder_name"));
                bankAccount.setBalance(rs.getBigDecimal("balance"));
                bankAccount.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return bankAccount;
            }, accountId);
            return Optional.ofNullable(account);
        } catch (Exception e) {
            logger.warn("Account not found for ID: {}", accountId);
            return Optional.empty();
        }
    }
}
