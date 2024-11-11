package com.sanlam.easybank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanlam.easybank.request.WithdrawRequest;
import com.sanlam.easybank.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BankAccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BankAccountService bankAccountService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testWithdrawSuccess() throws Exception {
        // Prepare the WithdrawRequest object
        WithdrawRequest request = new WithdrawRequest();
        request.setAccountId(1L);
        request.setAmount(new BigDecimal("50.00"));

        // Mock the service's withdrawal logic
        doNothing().when(bankAccountService).withdraw(any(Long.class), any(BigDecimal.class));

        // Perform the request
        mockMvc.perform(post("/bank/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Withdrawal successful. Current balance: 950.00"));
    }

    @Test
    public void testWithdrawFailureInsufficientFunds() throws Exception {
        // Prepare the WithdrawRequest object with excessive amount
        WithdrawRequest request = new WithdrawRequest();
        request.setAccountId(1L);
        request.setAmount(new BigDecimal("5000.00")); // Assuming this amount exceeds the account balance

        // Mock the withdrawal method to throw an exception for insufficient funds using doThrow
        doThrow(new RuntimeException("Insufficient funds"))
                .when(bankAccountService).withdraw(any(Long.class), any(BigDecimal.class));

        // Perform the request and expect HTTP 400 Bad Request
        mockMvc.perform(post("/bank/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // Expecting HTTP 400 for insufficient funds
    }
}
