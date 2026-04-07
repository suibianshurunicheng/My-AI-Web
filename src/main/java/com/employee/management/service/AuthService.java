package com.employee.management.service;

import com.employee.management.dto.LoginRequest;
import com.employee.management.dto.LoginResponse;
import com.employee.management.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "123456";
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public LoginResponse login(LoginRequest loginRequest) {
        if (ADMIN_USERNAME.equals(loginRequest.getUsername()) && 
            ADMIN_PASSWORD.equals(loginRequest.getPassword())) {
            
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            long expiresIn = jwtUtil.getExpirationTime();
            
            return new LoginResponse(token, loginRequest.getUsername(), expiresIn);
        } else {
            throw new RuntimeException("用户名或密码错误");
        }
    }
    
    public boolean verifyToken(String token) {
        return jwtUtil.validateToken(token);
    }
    
    public String getUsernameFromToken(String token) {
        return jwtUtil.getUsernameFromToken(token);
    }
}
