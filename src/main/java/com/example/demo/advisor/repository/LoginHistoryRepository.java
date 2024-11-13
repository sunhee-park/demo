package com.example.demo.advisor.repository;

import com.example.demo.advisor.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory , Long> {

}
