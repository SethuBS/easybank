package com.sanlam.easybank.model;

import com.sanlam.easybank.exception.InsufficientFundsException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "accounts")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private String accountHolderName;
    private BigDecimal balance;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Method to handle withdrawal and check for sufficient balance
    public void withdraw(BigDecimal amount) throws InsufficientFundsException {
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }
        balance = balance.subtract(amount);
    }

}
