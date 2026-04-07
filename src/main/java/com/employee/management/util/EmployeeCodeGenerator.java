package com.employee.management.util;

import com.employee.management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class EmployeeCodeGenerator {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    private static final int MAX_RETRY_COUNT = 10;
    
    public String generateUniqueEmployeeCode() {
        int retryCount = 0;
        String employeeCode;
        
        do {
            employeeCode = generateEmployeeCode();
            retryCount++;
            
            if (retryCount > MAX_RETRY_COUNT) {
                throw new RuntimeException("员工编码生成失败，已达到最大重试次数");
            }
            
        } while (employeeRepository.existsByEmployeeCode(employeeCode));
        
        return employeeCode;
    }
    
    private String generateEmployeeCode() {
        long timestamp = System.currentTimeMillis();
        String timestampPart = String.valueOf(timestamp).substring(7); // 取后6位
        
        Random random = new Random();
        String randomPart = String.format("%04d", random.nextInt(10000)); // 4位随机数，补零
        
        return timestampPart + randomPart;
    }
}