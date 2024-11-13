package com.example.demo.advisor.repository;

import com.example.demo.advisor.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findTop1ByPriceLessThanEqualOrderByPriceDesc(BigDecimal allocatedBalance);
    boolean existsByStockCode(String stockCode); // 중복 코드 확인
}
