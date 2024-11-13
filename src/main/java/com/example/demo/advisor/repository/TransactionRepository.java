package com.example.demo.advisor.repository;

import com.example.demo.advisor.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
