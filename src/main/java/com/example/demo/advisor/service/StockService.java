package com.example.demo.advisor.service;

import com.example.demo.advisor.entity.Stock;
import com.example.demo.advisor.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    // 증권 목록 조회
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    // 증권 등록
    @Transactional
    public Stock createStock(String stockCode, String stockName, BigDecimal price) {
        if (stockRepository.existsByStockCode(stockCode)) {
            throw new IllegalArgumentException("Stock with this code already exists.");
        }
        Stock stock = new Stock();
        stock.setStockCode(stockCode);
        stock.setName(stockName);
        stock.setPrice(price);
        return stockRepository.save(stock);
    }

    // 증권 가격 업데이트
    @Transactional
    public Stock updateStockPrice(Long stockId, BigDecimal newPrice) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        stock.setPrice(newPrice);
        return stockRepository.save(stock);
    }

    // 증권 삭제
    @Transactional
    public void deleteStock(Long stockId) {
        if (!stockRepository.existsById(stockId)) {
            throw new RuntimeException("Stock not found");
        }
        stockRepository.deleteById(stockId);
    }
}
