package com.sanlam.easybank.bootstrap;

import com.sanlam.easybank.model.BankAccount;
import com.sanlam.easybank.repository.BankAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader {

    private final BankAccountRepository bankAccountRepository;

    public DataLoader(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Bean
    CommandLineRunner initDatabase() {
        return args -> loadAccounts();
    }

    private void loadAccounts() {
        BankAccount bankAccount1 = new BankAccount();
        bankAccount1.setAccountNumber("1234567890");
        bankAccount1.setAccountHolderName("John Doe");
        bankAccount1.setBalance(BigDecimal.valueOf(1000));

        BankAccount bankAccount2 = new BankAccount();
        bankAccount2.setAccountNumber("0987654321");
        bankAccount2.setAccountHolderName("Jane Smith");
        bankAccount2.setBalance(BigDecimal.valueOf(1500));

        // Save accounts to the database
        bankAccountRepository.insertAccount(bankAccount1);
        bankAccountRepository.insertAccount(bankAccount2);

        System.out.println("Accounts loaded: ");
        System.out.println(bankAccount1);
        System.out.println(bankAccount2);
    }
}
