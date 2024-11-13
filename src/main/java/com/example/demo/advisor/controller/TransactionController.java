package com.example.demo.advisor.controller;

import com.example.demo.advisor.dto.TransactionResponse;
import com.example.demo.advisor.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        TransactionResponse transactionResponse = transactionService.deposit(userId, amount);
        return ResponseEntity.ok(transactionResponse);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        TransactionResponse transactionResponse = transactionService.withdraw(userId, amount);
        return ResponseEntity.ok(transactionResponse);
    }
}
