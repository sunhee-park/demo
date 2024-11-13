package com.example.demo.advisor.controller;

import com.example.demo.advisor.dto.AuthRequest;
import com.example.demo.advisor.dto.AuthResponse;
import com.example.demo.advisor.dto.RegisterRequest;
import com.example.demo.advisor.security.JwtUtil;
import com.example.demo.advisor.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
        String message = authService.registerUser(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(message);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest request) {
        String token = authService.authenticateUser(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestBody AuthRequest request) {

        String result = authService.logoutUser(request.getUsername());
        // 로그아웃은 클라이언트 쪽에서 JWT 토큰을 삭제하는 방식으로 간단하게 처리할 수 있습니다.
        return ResponseEntity.ok(result);
    }
}
