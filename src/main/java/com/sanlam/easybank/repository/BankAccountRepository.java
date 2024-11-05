package com.sanlam.easybank.repository;

import com.sanlam.easybank.model.BankAccount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@SuppressWarnings("ALL")
@Repository
public class BankAccountRepository {

    private final JdbcTemplate jdbcTemplate;

    public BankAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal getBalance(Long accountId) {
        String sql = "SELECT balance FROM accounts WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{accountId}, BigDecimal.class);
    }

    public int updateBalance(Long accountId, BigDecimal amount) {
        String sql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
        return jdbcTemplate.update(sql, amount, accountId);
    }

    // New method to insert an bankAccount for testing.
    public int insertAccount(BankAccount bankAccount) {
        String sql = "INSERT INTO accounts (account_number, account_holder_name, balance, created_at) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, bankAccount.getAccountNumber(), bankAccount.getAccountHolderName(), bankAccount.getBalance(), bankAccount.getCreatedAt());
    }

}
