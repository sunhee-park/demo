package com.example.demo.advisor.service;

import com.example.demo.advisor.dto.PortfolioResponse;
import com.example.demo.advisor.dto.PortfolioStockResponse;
import com.example.demo.advisor.entity.Account;
import com.example.demo.advisor.entity.Portfolio;
import com.example.demo.advisor.entity.PortfolioStock;
import com.example.demo.advisor.entity.Stock;
import com.example.demo.advisor.enums.RiskType;
import com.example.demo.advisor.repository.AccountRepository;
import com.example.demo.advisor.repository.PortfolioRepository;
import com.example.demo.advisor.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class AdviceService {

    private final AccountRepository accountRepository;
    private final PortfolioRepository portfolioRepository;
    private final StockRepository stockRepository;

    public AdviceService(AccountRepository accountRepository, PortfolioRepository portfolioRepository, StockRepository stockRepository) {
        this.accountRepository = accountRepository;
        this.portfolioRepository = portfolioRepository;
        this.stockRepository = stockRepository;
    }

    @Transactional
    public PortfolioResponse requestAdvice(Long userId, RiskType riskType) {
        Account account = accountRepository.findByUsersId(userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BigDecimal currentBalance = account.getBalance();
        BigDecimal allocatedBalance;

        // 위험 유형에 따른 포트폴리오 잔고 설정
        if (riskType == RiskType.TYPE1) {
            allocatedBalance = currentBalance.multiply(new BigDecimal("0.95")); // 잔고의 95% 사용 가정
        } else if (riskType == RiskType.TYPE2) {
            allocatedBalance = currentBalance.multiply(new BigDecimal("0.5")); // 잔고의 50% 사용 가정
        } else {
            throw new IllegalArgumentException("Invalid risk type");
        }

        // 포트폴리오 생성 및 저장 (실제 잔고 변경 없음)
        Portfolio portfolio = new Portfolio();
        portfolio.setUser(account.getUsers());
        portfolio.setRiskType(riskType.name());

        BigDecimal totalInvestment = BigDecimal.ZERO; // 초기화하여 주식 매입 금액 합계를 계산

        // 예시로 포트폴리오에 증권 추가
        Stock stock = stockRepository.findTop1ByPriceLessThanEqualOrderByPriceDesc(allocatedBalance)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        int quantity = calculateQuantity(stock, allocatedBalance);
        BigDecimal investment = stock.getPrice().multiply(new BigDecimal(quantity));
        totalInvestment = totalInvestment.add(investment);

        PortfolioStock portfolioStock = new PortfolioStock();
        portfolioStock.setPortfolio(portfolio);
        portfolioStock.setStock(stock);
        portfolioStock.setQuantity(quantity);
        portfolio.getPortfolioStocks().add(portfolioStock);

        // 전체 주식 매입 금액 설정
        portfolio.setTotalInvestment(totalInvestment);
        portfolioRepository.save(portfolio);

        // Portfolio를 PortfolioResponse로 변환
        return new PortfolioResponse(
                portfolio.getId(),
                portfolio.getRiskType(),
                portfolio.getPortfolioStocks().stream()
                        .map(s -> new PortfolioStockResponse(
                                s.getStock().getStockCode(),
                                s.getStock().getName(),
                                s.getStock().getPrice(),
                                s.getQuantity()
                        ))
                        .collect(Collectors.toList()),
                totalInvestment
        );
    }

    private int calculateQuantity(Stock stock, BigDecimal allocatedBalance) {
        return allocatedBalance.divide(stock.getPrice(), BigDecimal.ROUND_DOWN).intValue();
    }
}
