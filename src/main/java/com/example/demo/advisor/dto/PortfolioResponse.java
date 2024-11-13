package com.example.demo.advisor.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PortfolioResponse {
    private Long id;
    private String riskType;
    private List<PortfolioStockResponse> portfolioStocks;
    private BigDecimal totalInvestment;  // 총 투자 금액

    public PortfolioResponse(Long id, String riskType, List<PortfolioStockResponse> portfolioStocks, BigDecimal totalInvestment) {
        this.id = id;
        this.riskType = riskType;
        this.portfolioStocks = portfolioStocks;
        this.totalInvestment = totalInvestment;
    }
}

