package com.sanlam.easybank;

import com.sanlam.easybank.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankAccountRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private BankAccountRepository bankAccountRepository;

    private Long accountId;
    private BigDecimal initialBalance;

    @BeforeEach
    void setUp() {
        accountId = 1L;
        initialBalance = BigDecimal.valueOf(1000.00);
    }

    @Test
    void testGetBalance() {
        // Arrange
        when(jdbcTemplate.queryForObject(any(String.class), eq(BigDecimal.class), eq(accountId)))
                .thenReturn(initialBalance);

        // Act
        BigDecimal balance = bankAccountRepository.getBalance(accountId);

        // Assert
        assertEquals(initialBalance, balance);
        verify(jdbcTemplate, times(1)).queryForObject(any(String.class), eq(BigDecimal.class), eq(accountId));
    }

    @Test
    void testUpdateBalance() {
        // Arrange
        BigDecimal amountToDeduct = BigDecimal.valueOf(200.00);
        when(jdbcTemplate.update(any(String.class), eq(amountToDeduct), eq(accountId))).thenReturn(1);

        // Act
        int rowsAffected = bankAccountRepository.updateBalance(accountId, amountToDeduct);

        // Assert
        assertEquals(1, rowsAffected);
        verify(jdbcTemplate, times(1)).update(any(String.class), eq(amountToDeduct), eq(accountId));
    }

}
