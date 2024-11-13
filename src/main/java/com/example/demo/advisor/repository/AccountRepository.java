package com.example.demo.advisor.repository;

import com.example.demo.advisor.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsersId(Long userId);
}
