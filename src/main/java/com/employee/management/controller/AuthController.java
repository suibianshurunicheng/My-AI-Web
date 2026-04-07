package com.employee.management.controller;

import com.employee.management.annotation.PreventDuplicateSubmit;
import com.employee.management.dto.ApiResponse;
import com.employee.management.dto.LoginRequest;
import com.employee.management.dto.LoginResponse;
import com.employee.management.dto.VerifyTokenResponse;
import com.employee.management.service.AuthService;
import com.employee.management.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private LogService logService;
    
    @PostMapping("/login")
    @PreventDuplicateSubmit(interval = 3000, message = "请勿频繁登录")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        String clientIp = getClientIp(request);
        logger.info("收到登录请求: {}, IP: {}", loginRequest.getUsername(), clientIp);
        try {
            LoginResponse loginResponse = authService.login(loginRequest);
            logger.info("登录成功: {}", loginRequest.getUsername());
            logService.logLogin(loginRequest.getUsername(), "success", clientIp);
            return ApiResponse.success("登录成功", loginResponse);
        } catch (RuntimeException e) {
            logger.error("登录失败: {}", e.getMessage());
            logService.logLogin(loginRequest.getUsername(), "failed", clientIp);
            return ApiResponse.error(401, e.getMessage());
        }
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    @GetMapping("/verify")
    public ApiResponse<VerifyTokenResponse> verifyToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ApiResponse.error(401, "无效的 token");
        }
        
        String token = authHeader.substring(7);
        boolean isValid = authService.verifyToken(token);
        
        if (isValid) {
            String username = authService.getUsernameFromToken(token);
            VerifyTokenResponse response = new VerifyTokenResponse(username, true);
            return ApiResponse.success("Token 有效", response);
        } else {
            return ApiResponse.error(401, "Token 无效或已过期");
        }
    }
    
    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        logger.info("用户登出");
        return ApiResponse.success("登出成功", null);
    }
}
