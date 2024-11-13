package com.example.demo.advisor.dto;

import com.example.demo.advisor.entity.Transaction;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    private Long id;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private LocalDateTime timestamp;
    private String transactionType;

    // Transaction 객체를 매개변수로 받는 정적 팩토리 메서드
    public static TransactionResponse fromEntity(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getBalanceAfter(),
                transaction.getTimestamp(),
                transaction.getTransactionType()
        );
    }

    // 필요한 생성자 추가
    public TransactionResponse(Long id, BigDecimal amount, BigDecimal balanceAfter, LocalDateTime timestamp, String transactionType) {
        this.id = id;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = timestamp;
        this.transactionType = transactionType;
    }
}

