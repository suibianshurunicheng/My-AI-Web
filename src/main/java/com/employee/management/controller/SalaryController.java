package com.employee.management.controller;

import com.employee.management.annotation.PreventDuplicateSubmit;
import com.employee.management.dto.ApiResponse;
import com.employee.management.dto.ExportRequest;
import com.employee.management.dto.PageResponse;
import com.employee.management.entity.Payslip;
import com.employee.management.entity.SalaryStructure;
import com.employee.management.service.SalaryService;
import com.employee.management.util.ExportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {

    private static final Logger logger = LoggerFactory.getLogger(SalaryController.class);

    @Autowired
    private SalaryService salaryService;

    @GetMapping("/structures")
    public ApiResponse<List<SalaryStructure>> getStructures() {
        try {
            return ApiResponse.success(salaryService.getAllStructures());
        } catch (Exception e) {
            logger.error("获取薪资结构失败", e);
            return ApiResponse.error(500, "服务器内部错误");
        }
    }

    @PostMapping("/structures")
    @PreventDuplicateSubmit(interval = 2000, message = "请勿重复提交")
    public ApiResponse<SalaryStructure> saveStructure(@RequestBody SalaryStructure structure) {
        try {
            return ApiResponse.success(salaryService.saveStructure(structure));
        } catch (Exception e) {
            logger.error("保存薪资结构失败", e);
            return ApiResponse.error(500, "服务器内部错误");
        }
    }

    @DeleteMapping("/structures/{id}")
    public ApiResponse<Void> deleteStructure(@PathVariable Long id) {
        try {
            salaryService.deleteStructure(id);
                    return ApiResponse.success(null);
        } catch (Exception e) {
            logger.error("删除薪资结构失败", e);
            return ApiResponse.error(500, "服务器内部错误");
        }
    }

    @GetMapping("/payslips")
    public ApiResponse<PageResponse<Payslip>> getPayslips(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer monthNum,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Payslip> payslipPage = salaryService.getPayslips(employeeId, year, monthNum, page, size);
            PageResponse<Payslip> pageResponse = new PageResponse<Payslip>(
                    payslipPage.getTotalElements(),
                    page,
                    size,
                    payslipPage.getContent()
            );
            return ApiResponse.success(pageResponse);
        } catch (Exception e) {
            logger.error("获取工资条列表失败", e);
            return ApiResponse.error(500, "服务器内部错误");
        }
    }

    @GetMapping("/payslips/{id}")
    public ApiResponse<Payslip> getPayslipById(@PathVariable Long id) {
        try {
            Optional<Payslip> payslip = salaryService.getPayslipById(id);   
            return payslip.map(ApiResponse::success)
                    .orElse(ApiResponse.error(404, "工资条不存在"));
        } catch (Exception e) {
            logger.error("获取工资条详情失败", e);
            return ApiResponse.error(500, "服务器内部错误");
        }
    }

    @PostMapping("/payslips/generate")
    @PreventDuplicateSubmit(interval = 5000, message = "请勿重复生成工资条")
    public ApiResponse<List<Payslip>> generatePayslips(@RequestBody Map<String, Object> request) {
        try {
            Integer year = (Integer) request.get("year");
            Integer monthNum = (Integer) request.get("monthNum");
            @SuppressWarnings("unchecked")
            List<Long> employeeIds = (List<Long>) request.get("employeeIds");

            if (year == null || monthNum == null) {
                return ApiResponse.error(400, "年份和月份不能为空");
            }

            List<Payslip> payslips = salaryService.generatePayslips(year, monthNum, employeeIds);
            return ApiResponse.success(payslips);
        } catch (Exception e) {
            logger.error("生成工资条失败", e);
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    @PostMapping("/payslips/export")
    public ResponseEntity<byte[]> exportPayslips(@RequestBody ExportRequest exportRequest) {
        logger.info("导出工资条: format={}", exportRequest.getFormat());
        try {
            Map<String, Object> filters = exportRequest.getFilters() != null ? 
                    exportRequest.getFilters() : null;
            Integer year = filters != null && filters.get("year") != null ? 
                    Integer.parseInt(filters.get("year").toString()) : null;
            Integer monthNum = filters != null && filters.get("monthNum") != null ? 
                    Integer.parseInt(filters.get("monthNum").toString()) : null;

            List<Payslip> payslips = salaryService.getPayslipsForExport(year, monthNum);
            List<String> fields = exportRequest.getFields();

            byte[] data;
            String filename;
            String contentType;

            if ("csv".equalsIgnoreCase(exportRequest.getFormat())) {
                data = ExportUtil.exportPayslipsToCsv(payslips, fields);
                filename = "payslips.csv";
                contentType = "text/csv";
            } else {
                data = ExportUtil.exportPayslipsToExcel(payslips, fields);
                filename = "payslips.xlsx";
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDispositionFormData("attachment", new String(filename.getBytes("UTF-8"), "ISO-8859-1"));
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("导出工资条失败", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

