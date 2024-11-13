package com.example.demo.advisor.entity;

import com.example.demo.advisor.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Transaction extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    private BigDecimal amount;
    private BigDecimal balanceAfter;    // 거래 후 잔고

    private LocalDateTime timestamp;
    private String transactionType;


    public Transaction(Account account, BigDecimal amount, BigDecimal newBalance, TransactionType transactionType) {
        this.account = account;
        this.amount = amount;
        this.balanceAfter = newBalance;
        this.timestamp = LocalDateTime.now();
        this.transactionType = transactionType.name();
    }
}
