package com.example.demo.advisor.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class LoginHistory{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Users users;

    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;

    public LoginHistory(Users users, LocalDateTime loginTime, LocalDateTime logoutTime) {
        this.users = users;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
    }
}

