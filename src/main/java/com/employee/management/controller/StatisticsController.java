package com.employee.management.controller;

import com.employee.management.dto.*;
import com.employee.management.entity.Employee;
import com.employee.management.service.StatisticsService;
import com.employee.management.util.ExportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/employee-count")
    public ApiResponse<EmployeeCountDTO> getEmployeeCount() {
        try {
            EmployeeCountDTO result = statisticsService.getEmployeeCount();
            return ApiResponse.success(result);
        } catch (Exception e) {
            logger.error("获取员工总数失败", e);
            return ApiResponse.error(500, "获取员工总数失败: " + e.getMessage());
        }
    }

    @GetMapping("/gender-ratio")
    public ApiResponse<List<GenderRatioDTO>> getGenderRatio() {
        try {
            List<GenderRatioDTO> result = statisticsService.getGenderRatio();
            return ApiResponse.success(result);
        } catch (Exception e) {
            logger.error("获取男女比例失败", e);
            return ApiResponse.error(500, "获取男女比例失败: " + e.getMessage());
        }
    }

    @GetMapping("/department-count")
    public ApiResponse<List<DepartmentCountDTO>> getDepartmentCount() {
        try {
            List<DepartmentCountDTO> result = statisticsService.getDepartmentCount();
            return ApiResponse.success(result);
        } catch (Exception e) {
            logger.error("获取部门人数失败", e);
            return ApiResponse.error(500, "获取部门人数失败: " + e.getMessage());
        }
    }

    @GetMapping("/age-distribution")
    public ApiResponse<List<AgeDistributionDTO>> getAgeDistribution() {
        try {
            List<AgeDistributionDTO> result = statisticsService.getAgeDistribution();
            return ApiResponse.success(result);
        } catch (Exception e) {
            logger.error("获取年龄分布失败", e);
            return ApiResponse.error(500, "获取年龄分布失败: " + e.getMessage());
        }
    }

    @GetMapping("/turnover/monthly")
    public ApiResponse<List<TurnoverMonthlyDTO>> getTurnoverByMonth(@RequestParam(required = false) Integer year) {
        try {
            List<TurnoverMonthlyDTO> result = statisticsService.getTurnoverByMonth(year);
            return ApiResponse.success(result);
        } catch (Exception e) {
            logger.error("获取月度离职率失败", e);
            return ApiResponse.error(500, "获取月度离职率失败: " + e.getMessage());
        }
    }

    @GetMapping("/turnover/department")
    public ApiResponse<List<TurnoverDepartmentDTO>> getTurnoverByDepartment(@RequestParam(required = false) Integer year) {
        try {
            List<TurnoverDepartmentDTO> result = statisticsService.getTurnoverByDepartment(year);
            return ApiResponse.success(result);
        } catch (Exception e) {
            logger.error("获取部门离职率失败", e);
            return ApiResponse.error(500, "获取部门离职率失败: " + e.getMessage());
        }
    }

    @PostMapping("/custom-report/export")
    public ResponseEntity<byte[]> exportCustomReport(@RequestBody ExportRequest exportRequest) {
        logger.info("导出自定义报表: format={}, fields={}", exportRequest.getFormat(), exportRequest.getFields());
        try {
            Map<String, Object> filters = exportRequest.getFilters();
            List<Employee> employees = statisticsService.getEmployeesForCustomReport(filters);
            List<String> fields = exportRequest.getFields();

            byte[] data;
            String filename;
            String contentType;

            if ("csv".equalsIgnoreCase(exportRequest.getFormat())) {
                data = ExportUtil.exportToCsv(employees, fields);
                filename = "custom-report.csv";
                contentType = "text/csv";
            } else {
                data = ExportUtil.exportToExcel(employees, fields);
                filename = "custom-report.xlsx";
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
            logger.error("导出自定义报表失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
