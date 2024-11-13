package com.example.demo.advisor.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PortfolioStockResponse {
    private String stockCode;
    private String stockName;
    private BigDecimal price;
    private int quantity;

    public PortfolioStockResponse(String stockCode, String stockName, BigDecimal price, int quantity) {
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.price = price;
        this.quantity = quantity;
    }
}
