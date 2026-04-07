package com.employee.management.controller;

import com.employee.management.annotation.PreventDuplicateSubmit;
import com.employee.management.dto.ApiResponse;
import com.employee.management.dto.ExportRequest;
import com.employee.management.dto.PageResponse;
import com.employee.management.entity.Employee;
import com.employee.management.service.EmployeeService;
import com.employee.management.util.ExportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    
    @Autowired
    private EmployeeService employeeService;
    
    @GetMapping
    public ApiResponse<PageResponse<Employee>> getEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("收到请求 - name: {}, code: {}, page: {}, size: {}", name, code, page, size);
        try {
            String searchName = (name != null && !name.trim().isEmpty()) ? name.trim() : null;
            String searchCode = (code != null && !code.trim().isEmpty()) ? code.trim() : null;
            
            logger.info("处理后 - searchName: {}, searchCode: {}", searchName, searchCode);
            
            Page<Employee> employeePage = employeeService.searchEmployeesWithPagination(searchName, searchCode, page, size);
            logger.info("查询结果 - 总数: {}", employeePage.getTotalElements());
            
            PageResponse<Employee> pageResponse = new PageResponse<>(
                    employeePage.getTotalElements(),
                    page,
                    size,
                    employeePage.getContent()
            );
            
            return ApiResponse.success(pageResponse);
        } catch (Exception e) {
            logger.error("查询员工列表失败", e);
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Employee> getEmployeeById(@PathVariable Long id) {
        try {
            Optional<Employee> employee = employeeService.findById(id);
            return employee.map(emp -> ApiResponse.success(emp))
                    .orElse(ApiResponse.error(404, "员工不存在"));
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误");
        }
    }
    
    @PostMapping
    @PreventDuplicateSubmit(interval = 3000, message = "请勿重复提交")
    public ApiResponse<Employee> createEmployee(@RequestBody Employee employee) {
        try {
            Employee savedEmployee = employeeService.save(employee);
            return ApiResponse.success(savedEmployee);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误");
        }
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Employee> updateEmployee(@PathVariable Long id, 
                                               @RequestBody Employee employeeDetails) {
        try {
            Employee updatedEmployee = employeeService.update(id, employeeDetails);
            return ApiResponse.success(updatedEmployee);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("员工不存在")) {
                return ApiResponse.error(404, e.getMessage());
            }
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误");
        }
    }
    
    @DeleteMapping("/{id}")
    @PreventDuplicateSubmit(interval = 2000, message = "请勿重复提交")
    public ApiResponse<Void> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteById(id);
            return ApiResponse.success("员工已成功删除", null);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("员工不存在")) {
                return ApiResponse.error(404, e.getMessage());
            }
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误");
        }
    }
    
    @PostMapping("/export")
    public ResponseEntity<byte[]> exportEmployees(@RequestBody ExportRequest exportRequest) {
        logger.info("导出员工数据: format={}, fields={}", exportRequest.getFormat(), exportRequest.getFields());
        try {
            Map<String, Object> filters = exportRequest.getFilters();
            String name = filters != null && filters.get("name") != null ? filters.get("name").toString() : null;
            String code = filters != null && filters.get("code") != null ? filters.get("code").toString() : null;       
            
            List<Employee> employees = employeeService.getEmployeesForExport(name, code);
            List<String> fields = exportRequest.getFields();
            
            byte[] data;
            String filename;
            String contentType;
            
            if ("csv".equalsIgnoreCase(exportRequest.getFormat())) {
                data = ExportUtil.exportToCsv(employees, fields);
                filename = "employees.csv";
                contentType = "text/csv";
            } else {
                data = ExportUtil.exportToExcel(employees, fields);
                filename = "employees.xlsx";
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDispositionFormData("attachment", new String(filename.getBytes("UTF-8"), "ISO-8859-1"));
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(data);
        } catch (Exception e) {
            logger.error("导出员工数据失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
