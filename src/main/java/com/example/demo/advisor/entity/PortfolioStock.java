package com.example.demo.advisor.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class PortfolioStock extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Portfolio portfolio;

    @ManyToOne
    private Stock stock;

    private Integer quantity;

}
