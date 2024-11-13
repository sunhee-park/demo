package com.example.demo.advisor.service;

import com.example.demo.advisor.entity.Account;
import com.example.demo.advisor.entity.LoginHistory;
import com.example.demo.advisor.entity.Users;
import com.example.demo.advisor.exception.AuthenticationFailedException;
import com.example.demo.advisor.exception.UserAlreadyExistsException;
import com.example.demo.advisor.repository.AccountRepository;
import com.example.demo.advisor.repository.LoginHistoryRepository;
import com.example.demo.advisor.repository.UsersRepository;
import com.example.demo.advisor.security.CustomUserDetails;
import com.example.demo.advisor.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final LoginHistoryRepository loginHistoryRepository;
    private final UsersRepository usersRepository;
    private final AccountRepository accountRepository;

    public AuthService(UsersRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager, LoginHistoryRepository loginHistoryRepository, UsersRepository usersRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.loginHistoryRepository = loginHistoryRepository;
        this.usersRepository = usersRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public String registerUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        Account account = new Account();
        account.setUsers(user);
        account.setBalance(new BigDecimal(0));
        accountRepository.save(account);
        return "User registered successfully";
    }

    public String authenticateUser(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 로그인 기록 저장
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal(); // UserDetails에서 User 객체를 가져오기 또는 CustomUserDetails로 캐스팅 가능
            LoginHistory loginHistory = new LoginHistory(userDetails.getUsers(), LocalDateTime.now(), null);
            loginHistoryRepository.save(loginHistory);

            return jwtUtil.generateToken(username);

        } catch (BadCredentialsException e) {
            throw new AuthenticationFailedException("Invalid username or password", e);
        }
    }

    public String logoutUser(String username) {

        Users users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // 로그아웃 기록 저장
        LoginHistory loginHistory = new LoginHistory(users, null , LocalDateTime.now());
        loginHistoryRepository.save(loginHistory);

        return "User logged out successfully";
    }
}
