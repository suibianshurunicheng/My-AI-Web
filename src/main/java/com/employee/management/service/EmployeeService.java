package com.employee.management.service;

import com.employee.management.dto.CursorPageRequest;
import com.employee.management.dto.CursorPageResponse;
import com.employee.management.dto.PageResponse;
import com.employee.management.entity.Employee;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.util.EmployeeCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    
    // ============================================
    // 性能优化方法
    // ============================================
    
    /**
     * 游标分页查询（优化深度分页性能）
     */
    @Transactional(readOnly = true)
    public com.employee.management.dto.CursorPageResponse<Employee> searchEmployeesByCursor(
            com.employee.management.dto.CursorPageRequest pageRequest) {
        
        Long cursor = pageRequest.getCursor();
        Integer size = pageRequest.getSize();
        String sortField = pageRequest.getSortField();
        String sortDirection = pageRequest.getSortDirection();
        
        Sort sort = "asc".equalsIgnoreCase(sortDirection) ? 
                    Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(0, size, sort);
        
        List<Employee> employees;
        
        if (cursor == null || cursor == 0) {
            // 第一页
            employees = employeeRepository.findAll(pageable).getContent();
        } else {
            // 使用游标查询
            if ("asc".equalsIgnoreCase(sortDirection)) {
                employees = employeeRepository.findByIdGreaterThan(cursor, pageable);
            } else {
                employees = employeeRepository.findByIdLessThan(cursor, pageable);
            }
        }
        
        // 计算下一个游标
        Long nextCursor = null;
        if (!employees.isEmpty()) {
            nextCursor = employees.get(employees.size() - 1).getId();
        }
        
        return com.employee.management.dto.CursorPageResponse.of(employees, nextCursor, size);
    }
    
    /**
     * 延迟关联分页查询（优化深度分页）
     * 先查询 ID 列表，再关联查询详细信息
     */
    @Transactional(readOnly = true)
    public PageResponse<Employee> searchEmployeesOptimized(int page, int size) {
        int pageNumber = page - 1;
        Pageable pageable = PageRequest.of(pageNumber, size);
        
        // 第一步：查询 ID 列表
        List<Long> ids = employeeRepository.findIds(pageable);
        
        if (ids.isEmpty()) {
            return new PageResponse<>(0L, page, size, new java.util.ArrayList<>());
        }
        
        // 第二步：根据 ID 列表查询详细信息
        List<Employee> employees = employeeRepository.findByIds(ids);
        
        // 保持 ID 列表的顺序
        employees.sort((e1, e2) -> {
            int index1 = ids.indexOf(e1.getId());
            int index2 = ids.indexOf(e2.getId());
            return Integer.compare(index1, index2);
        });
        
        // 计算总数
        long total = employeeRepository.count();
        
        return new PageResponse<>(total, page, size, employees);
    }
    
    /**
     * 批量保存员工数据（优化批量写入性能）
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Employee> batchSave(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        
        // 验证数据
        for (Employee employee : employees) {
            validateEmployee(employee);
        }
        
        // 批量生成工号
        for (Employee employee : employees) {
            if (employee.getEmployeeCode() == null) {
                employee.setEmployeeCode(employeeCodeGenerator.generateUniqueEmployeeCode());
            }
        }
        
        // 批量保存
        return employeeRepository.saveAll(employees);
    }
    
    /**
     * 验证员工数据
     */
    private void validateEmployee(Employee employee) {
        if (employee.getName() == null || employee.getName().trim().isEmpty()) {
            throw new RuntimeException("员工姓名不能为空");
        }
        if (employee.getEmail() == null || employee.getEmail().trim().isEmpty()) {
            throw new RuntimeException("员工邮箱不能为空");
        }
        if (employee.getAge() != null && (employee.getAge() < 18 || employee.getAge() > 65)) {
            throw new RuntimeException("员工年龄必须在 18-65 岁之间");
        }
    }
}