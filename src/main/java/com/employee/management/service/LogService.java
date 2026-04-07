package com.employee.management.service;

import com.employee.management.entity.LoginLog;
import com.employee.management.entity.OperationLog;
import com.employee.management.repository.LoginLogRepository;
import com.employee.management.repository.OperationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LogService {

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Autowired
    private LoginLogRepository loginLogRepository;

    public Page<OperationLog> getOperationLogs(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return operationLogRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Page<LoginLog> getLoginLogs(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return loginLogRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public OperationLog logOperation(String username, String operation, String details, String ip) {
        OperationLog log = new OperationLog();
        log.setUsername(username);
        log.setOperation(operation);
        log.setDetails(details);
        log.setIp(ip);
        return operationLogRepository.save(log);
    }

    public LoginLog logLogin(String username, String status, String ip) {
        LoginLog log = new LoginLog();
        log.setUsername(username);
        log.setStatus(status);
        log.setIp(ip);
        return loginLogRepository.save(log);
    }
}
