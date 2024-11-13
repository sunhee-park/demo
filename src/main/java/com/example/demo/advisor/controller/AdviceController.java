package com.example.demo.advisor.controller;

import com.example.demo.advisor.dto.PortfolioResponse;
import com.example.demo.advisor.enums.RiskType;
import com.example.demo.advisor.service.AdviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/advice")
public class AdviceController {

    private final AdviceService adviceService;

    public AdviceController(AdviceService adviceService) {
        this.adviceService = adviceService;
    }

    @PostMapping("/request")
    public ResponseEntity<PortfolioResponse> requestAdvice(@RequestParam Long userId, @RequestParam RiskType riskType) {
        PortfolioResponse portfolioResponse = adviceService.requestAdvice(userId, riskType);
        return ResponseEntity.ok(portfolioResponse);
    }
}
