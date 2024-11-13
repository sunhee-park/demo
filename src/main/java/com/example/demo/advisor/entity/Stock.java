package com.example.demo.advisor.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
public class Stock extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockCode;
    private String name;
    private BigDecimal price;

    public Stock(String stockCode, String name, BigDecimal price) {
        this.stockCode = stockCode;
        this.name = name;
        this.price = price;
    }
}

