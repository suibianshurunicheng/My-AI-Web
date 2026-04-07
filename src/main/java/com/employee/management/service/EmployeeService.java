package com.employee.management.service;

import com.employee.management.entity.Employee;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.util.EmployeeCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private EmployeeCodeGenerator employeeCodeGenerator;
    
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }
    
    public Page<Employee> findAll(Pageable pageable) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 10);
        }
        return employeeRepository.findAll(pageable);
    }
    
    public Optional<Employee> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return employeeRepository.findById(id);
    }
    
    public Optional<Employee> findByEmployeeCode(String employeeCode) {
        return employeeRepository.findByEmployeeCode(employeeCode);
    }
    
    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }
    
    public Employee save(Employee employee) {
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new RuntimeException("邮箱已存在: " + employee.getEmail());
        }
        
        String employeeCode = employeeCodeGenerator.generateUniqueEmployeeCode();
        employee.setEmployeeCode(employeeCode);
        
        return employeeRepository.save(employee);
    }
    
    public Employee createEmployee(Employee employee) {
        return save(employee);
    }
    
    public Employee createEmployeeFromOffer(Employee employee) {
        // 跳过邮箱检查，直接保存
        String employeeCode = employeeCodeGenerator.generateUniqueEmployeeCode();
        employee.setEmployeeCode(employeeCode);
        
        return employeeRepository.save(employee);
    }
    
    public Employee update(Long id, Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("员工不存在，ID: " + id));
        
        if (employeeDetails.getEmail() != null && 
            !employeeDetails.getEmail().equals(employee.getEmail()) && 
            employeeRepository.existsByEmailAndIdNot(employeeDetails.getEmail(), id)) {
            throw new RuntimeException("邮箱已存在: " + employeeDetails.getEmail());
        }
        
        if (employeeDetails.getName() != null) {
            employee.setName(employeeDetails.getName());
        }
        if (employeeDetails.getGender() != null) {
            employee.setGender(employeeDetails.getGender());
        }
        if (employeeDetails.getAge() != null) {
            employee.setAge(employeeDetails.getAge());
        }
        if (employeeDetails.getBirthDate() != null) {
            employee.setBirthDate(employeeDetails.getBirthDate());
        }
        if (employeeDetails.getHeight() != null) {
            employee.setHeight(employeeDetails.getHeight());
        }
        if (employeeDetails.getWeight() != null) {
            employee.setWeight(employeeDetails.getWeight());
        }
        if (employeeDetails.getPosition() != null) {
            employee.setPosition(employeeDetails.getPosition());
        }
        if (employeeDetails.getEmail() != null) {
            employee.setEmail(employeeDetails.getEmail());
        }
        if (employeeDetails.getPhone() != null) {
            employee.setPhone(employeeDetails.getPhone());
        }
        
        return employeeRepository.save(employee);
    }
    
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("员工ID不能为空");
        }
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("员工不存在，ID: " + id);
        }
        employeeRepository.deleteById(id);
    }
    
    public List<Employee> searchEmployees(String name, String code) {
        if (name != null && !name.trim().isEmpty() && code != null && !code.trim().isEmpty()) {
            return employeeRepository.findByNameAndEmployeeCodeContaining(name, code);
        } else if (name != null && !name.trim().isEmpty()) {
            return employeeRepository.findByNameContaining(name);
        } else if (code != null && !code.trim().isEmpty()) {
            return employeeRepository.findByEmployeeCodeContaining(code);
        } else {
            return employeeRepository.findAll();
        }
    }
    
    public Page<Employee> searchEmployeesWithPagination(String name, String code, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        
        if (name != null && !name.trim().isEmpty() && code != null && !code.trim().isEmpty()) {
            List<Employee> employees = employeeRepository.findByNameAndEmployeeCodeContaining(name, code);
            return new org.springframework.data.domain.PageImpl<>(employees, pageable, employees.size());
        } else if (name != null && !name.trim().isEmpty()) {
            List<Employee> employees = employeeRepository.findByNameContaining(name);
            return new org.springframework.data.domain.PageImpl<>(employees, pageable, employees.size());
        } else if (code != null && !code.trim().isEmpty()) {
            List<Employee> employees = employeeRepository.findByEmployeeCodeContaining(code);
            return new org.springframework.data.domain.PageImpl<>(employees, pageable, employees.size());
        } else {
            return employeeRepository.findAll(pageable);
        }
    }
    
    public long getTotalEmployeeCount() {
        return employeeRepository.count();
    }
    
    public List<Employee> getEmployeesForExport(String name, String code) {
        return searchEmployees(name, code);
    }
}