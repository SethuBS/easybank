package com.sanlam.easybank.controller;

import com.sanlam.easybank.exception.InsufficientFundsException;
import com.sanlam.easybank.request.WithdrawRequest;
import com.sanlam.easybank.service.BankAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/bank")
public class BankAccountController {


    private static final Logger logger = LogManager.getLogger(BankAccountController.class);

    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        Long accountId = withdrawRequest.getAccountId();
        BigDecimal amount = withdrawRequest.getAmount();

        BigDecimal currentBalance = bankAccountService.getBalance(accountId); // Get current balance

        logger.info("Received withdrawal request for account {} with amount {}", accountId, amount);

        if (accountId == null || amount == null) {
            logger.warn("Withdrawal request failed: Account ID or amount is missing.");
            return ResponseEntity.badRequest().body("Account ID and amount are required.");
        }

        try {
            bankAccountService.withdraw(accountId, amount);
            logger.info("Withdrawal successful for account {}", accountId);
            String responseMessage = String.format("Withdrawal successful. Current balance: %s", currentBalance);
            return ResponseEntity.ok(responseMessage);
        } catch (InsufficientFundsException e) {
            logger.warn("Insufficient funds for account {}. Current balance: {}. Error: {}", accountId, currentBalance, e.getMessage());
            String response = String.format("Insufficient funds. Current balance: %s", currentBalance);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error("Withdrawal failed for account {}: {}", accountId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Withdrawal failed");
        }
    }
}
