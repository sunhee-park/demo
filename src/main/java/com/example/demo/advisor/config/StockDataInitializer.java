package com.example.demo.advisor.config;

import com.example.demo.advisor.entity.Stock;
import com.example.demo.advisor.repository.StockRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Configuration
public class StockDataInitializer {

    @Bean
    public CommandLineRunner initStocks(StockRepository stockRepository) {
        return args -> {
            if (stockRepository.count() < 10) {
                List<Stock> initialStocks = Arrays.asList(
                        new Stock("AAPL", "Apple Inc.", new BigDecimal("150.00")),
                        new Stock("GOOGL", "Alphabet Inc.", new BigDecimal("2800.00")),
                        new Stock("AMZN", "Amazon.com Inc.", new BigDecimal("3400.00")),
                        new Stock("MSFT", "Microsoft Corp.", new BigDecimal("300.00")),
                        new Stock("TSLA", "Tesla Inc.", new BigDecimal("700.00")),
                        new Stock("FB", "Meta Platforms Inc.", new BigDecimal("350.00")),
                        new Stock("NFLX", "Netflix Inc.", new BigDecimal("590.00")),
                        new Stock("NVDA", "NVIDIA Corp.", new BigDecimal("220.00")),
                        new Stock("BABA", "Alibaba Group", new BigDecimal("160.00")),
                        new Stock("V", "Visa Inc.", new BigDecimal("230.00"))
                );
                stockRepository.saveAll(initialStocks);
            }
        };
    }
}

