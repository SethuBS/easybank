package com.sanlam.easybank.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class WithdrawalEvent {

    private BigDecimal amount;
    private Long accountId;
    private String status;
    private LocalDateTime timestamp = LocalDateTime.now();

    public WithdrawalEvent(BigDecimal amount, Long accountId, String status) {
        this.amount = amount;
        this.accountId = accountId;
        this.status = status;
    }

    public String toJson() {
        return String.format(
                "{\"amount\":\"%s\",\"accountId\":%d,\"status\":\"%s\",\"timestamp\":\"%s\"}",
                amount, accountId, status, timestamp
        );
    }
}
