package com.example.demo.advisor.service;

import com.example.demo.advisor.dto.TransactionResponse;
import com.example.demo.advisor.entity.Account;
import com.example.demo.advisor.entity.Transaction;
import com.example.demo.advisor.enums.TransactionType;
import com.example.demo.advisor.repository.AccountRepository;
import com.example.demo.advisor.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransactionResponse deposit(Long userId, BigDecimal amount) {
        try {
            Account account = accountRepository.findByUsersId(userId)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            BigDecimal newBalance = account.getBalance().add(amount);
            account.setBalance(newBalance);
            accountRepository.save(account);

            Transaction transaction = new Transaction(account, amount, newBalance, TransactionType.DEPOSIT);
            transactionRepository.save(transaction);
            return TransactionResponse.fromEntity(transaction);

        } catch (ObjectOptimisticLockingFailureException e) {
            // 낙관적 락 충돌이 발생한 경우 예외 처리
            throw new RuntimeException("Failed to update account balance due to a concurrent modification", e);
        }
    }

    @Transactional
    public TransactionResponse withdraw(Long userId, BigDecimal amount) {
        try {
            Account account = accountRepository.findByUsersId(userId)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            if (account.getBalance().compareTo(amount) < 0) {
                throw new RuntimeException("Insufficient funds");
            }

            BigDecimal newBalance = account.getBalance().subtract(amount);
            account.setBalance(newBalance);
            accountRepository.save(account);

            Transaction transaction = new Transaction(account, amount, newBalance, TransactionType.WITHDRAWAL);
            transactionRepository.save(transaction);
            return TransactionResponse.fromEntity(transaction);

        } catch (ObjectOptimisticLockingFailureException e) {
            // 낙관적 락 충돌이 발생한 경우 예외 처리
            throw new RuntimeException("Failed to update account balance due to a concurrent modification", e);
        }
    }
}
