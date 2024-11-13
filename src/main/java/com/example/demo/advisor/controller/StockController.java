package com.example.demo.advisor.controller;

import com.example.demo.advisor.entity.Stock;
import com.example.demo.advisor.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // 전체 증권 목록 조회
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    // 증권 등록
    @PostMapping("/register")
    public ResponseEntity<Stock> createStock(@RequestParam String stockCode,
                                             @RequestParam String stockName,
                                             @RequestParam BigDecimal price) {
        Stock stock = stockService.createStock(stockCode, stockName, price);
        return ResponseEntity.ok(stock);
    }

    // 증권 가격 업데이트
    @PutMapping("/{stockId}/price")
    public ResponseEntity<Stock> updateStockPrice(@PathVariable Long stockId,
                                                  @RequestParam BigDecimal newPrice) {
        Stock updatedStock = stockService.updateStockPrice(stockId, newPrice);
        return ResponseEntity.ok(updatedStock);
    }

    // 증권 삭제
    @DeleteMapping("/{stockId}")
    public ResponseEntity<String> deleteStock(@PathVariable Long stockId) {
        stockService.deleteStock(stockId);
        return ResponseEntity.ok("Stock deleted successfully.");
    }
}

