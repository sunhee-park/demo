package com.example.demo.advisor.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class Account extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Users users;

    private BigDecimal balance; // 원화 잔고

    @Version // 낙관적 락 적용
    private Long version;

}
