package com.employee.management.controller;

import com.employee.management.dto.ApiResponse;
import com.employee.management.entity.LoginLog;
import com.employee.management.entity.OperationLog;
import com.employee.management.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private LogService logService;

    @GetMapping("/test")
    public String test() {
        logger.info("测试日志控制器");
        return "LogController 工作正常";
    }

    @GetMapping("/operation")
    public ApiResponse<Map<String, Object>> getOperationLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("获取操作日志: page={}, size={}", page, size);
        try {
            Page<OperationLog> logPage = logService.getOperationLogs(page, size);
            
            Map<String, Object> data = new HashMap<>();
            data.put("total", logPage.getTotalElements());
            data.put("page", page);
            data.put("size", size);
            data.put("list", logPage.getContent());
            
            return ApiResponse.success("获取操作日志成功", data);
        } catch (Exception e) {
            logger.error("获取操作日志失败", e);
            return ApiResponse.error(500, "获取操作日志失败: " + e.getMessage());
        }
    }

    @GetMapping("/login")
    public ApiResponse<Map<String, Object>> getLoginLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("获取登录日志: page={}, size={}", page, size);
        try {
            Page<LoginLog> logPage = logService.getLoginLogs(page, size);
            
            Map<String, Object> data = new HashMap<>();
            data.put("total", logPage.getTotalElements());
            data.put("page", page);
            data.put("size", size);
            data.put("list", logPage.getContent());
            
            return ApiResponse.success("获取登录日志成功", data);
        } catch (Exception e) {
            logger.error("获取登录日志失败", e);
            return ApiResponse.error(500, "获取登录日志失败: " + e.getMessage());
        }
    }

    @PostMapping("/operation")
    public ApiResponse<OperationLog> logOperation(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String operation = request.get("operation");
        String details = request.get("details");
        String ip = request.get("ip");
        
        logger.info("记录操作日志: username={}, operation={}", username, operation);
        try {
            OperationLog log = logService.logOperation(username, operation, details, ip);
            return ApiResponse.success("记录操作日志成功", log);
        } catch (Exception e) {
            logger.error("记录操作日志失败", e);
            return ApiResponse.error(500, "记录操作日志失败: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ApiResponse<LoginLog> logLogin(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String status = request.get("status");
        String ip = request.get("ip");
        
        logger.info("记录登录日志: username={}, status={}", username, status);
        try {
            LoginLog log = logService.logLogin(username, status, ip);
            return ApiResponse.success("记录登录日志成功", log);
        } catch (Exception e) {
            logger.error("记录登录日志失败", e);
            return ApiResponse.error(500, "记录登录日志失败: " + e.getMessage());
        }
    }
}
