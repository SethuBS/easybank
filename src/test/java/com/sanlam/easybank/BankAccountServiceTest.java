package com.sanlam.easybank;

import com.sanlam.easybank.exception.InsufficientFundsException;
import com.sanlam.easybank.model.WithdrawalEvent;
import com.sanlam.easybank.publisher.EventPublisher;
import com.sanlam.easybank.repository.BankAccountRepository;
import com.sanlam.easybank.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private BankAccountService bankAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testWithdrawSuccess() throws InsufficientFundsException {
        Long accountId = 1L;
        BigDecimal amount = new BigDecimal("50");

        when(bankAccountRepository.getBalance(accountId)).thenReturn(new BigDecimal("100"));
        when(bankAccountRepository.updateBalance(accountId, amount)).thenReturn(1);

        bankAccountService.withdraw(accountId, amount);
        WithdrawalEvent event = new WithdrawalEvent(amount, accountId, "SUCCESSFULL", LocalDateTime.now());
        eventPublisher.publish(event);

        verify(eventPublisher, times(1)).publish(any(WithdrawalEvent.class));
    }

    @Test
    void testWithdrawInsufficientFunds() {
        Long accountId = 1L;
        BigDecimal amount = new BigDecimal("150");

        when(bankAccountRepository.getBalance(accountId)).thenReturn(amount);

        doThrow(new RuntimeException("Insufficient funds"))
                .when(bankAccountService).withdraw(any(Long.class), any(BigDecimal.class));

    }
}
