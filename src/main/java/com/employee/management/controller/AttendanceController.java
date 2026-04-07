package com.employee.management.controller;

import com.employee.management.annotation.PreventDuplicateSubmit;
import com.employee.management.dto.ApiResponse;
import com.employee.management.entity.AttendanceRecord;
import com.employee.management.entity.LeaveApplication;
import com.employee.management.entity.OvertimeRecord;
import com.employee.management.service.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/records")
    public ApiResponse<Map<String, Object>> getAttendanceRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("获取打卡记录: page={}, size={}", page, size);
        try {
            Page<AttendanceRecord> recordPage = attendanceService.getAttendanceRecords(page, size);
            
            Map<String, Object> data = new HashMap<>();
            data.put("total", recordPage.getTotalElements());
            data.put("page", page);
            data.put("size", size);
            data.put("list", recordPage.getContent());
            
            return ApiResponse.success("获取打卡记录成功", data);
        } catch (Exception e) {
            logger.error("获取打卡记录失败", e);
            return ApiResponse.error(500, "获取打卡记录失败: " + e.getMessage());
        }
    }

    @PostMapping("/check-in")
    @PreventDuplicateSubmit(interval = 5000, message = "请勿重复打卡")
    public ApiResponse<AttendanceRecord> checkIn(@RequestBody Map<String, Object> request) {
        Long employeeId = Long.valueOf(request.get("employeeId").toString());
        String employeeName = (String) request.get("employeeName");
        
        logger.info("上班打卡: employeeId={}, employeeName={}", employeeId, employeeName);
        try {
            AttendanceRecord record = attendanceService.checkIn(employeeId, employeeName);
            return ApiResponse.success("上班打卡成功", record);
        } catch (Exception e) {
            logger.error("上班打卡失败", e);
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PostMapping("/check-out")
    public ApiResponse<AttendanceRecord> checkOut(@RequestBody Map<String, Object> request) {
        Long employeeId = Long.valueOf(request.get("employeeId").toString());
        String employeeName = (String) request.get("employeeName");
        
        logger.info("下班打卡: employeeId={}, employeeName={}", employeeId, employeeName);
        try {
            AttendanceRecord record = attendanceService.checkOut(employeeId, employeeName);
            return ApiResponse.success("下班打卡成功", record);
        } catch (Exception e) {
            logger.error("下班打卡失败", e);
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PostMapping("/makeup")
    public ApiResponse<AttendanceRecord> applyMakeup(@RequestBody Map<String, Object> request) {
        Long employeeId = Long.valueOf(request.get("employeeId").toString());
        String employeeName = (String) request.get("employeeName");
        LocalDate date = LocalDate.parse((String) request.get("date"));
        String type = (String) request.get("type");
        String reason = (String) request.get("reason");
        
        logger.info("补卡申请: employeeId={}, date={}, type={}", employeeId, date, type);
        try {
            AttendanceRecord record = attendanceService.applyMakeup(employeeId, employeeName, date, type, reason);
            return ApiResponse.success("补卡申请提交成功", record);
        } catch (Exception e) {
            logger.error("补卡申请失败", e);
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PutMapping("/makeup/{id}/approve")
    public ApiResponse<AttendanceRecord> approveMakeup(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        boolean approved = (Boolean) request.get("approved");
        String comment = (String) request.get("comment");
        
        logger.info("审批补卡: id={}, approved={}", id, approved);
        try {
            AttendanceRecord record = attendanceService.approveMakeup(id, approved, comment);
            return ApiResponse.success("补卡审批成功", record);
        } catch (Exception e) {
            logger.error("补卡审批失败", e);
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @GetMapping("/leave/applications")
    public ApiResponse<Map<String, Object>> getLeaveApplications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("获取请假申请: page={}, size={}", page, size);
        try {
            Page<LeaveApplication> applicationPage = attendanceService.getLeaveApplications(page, size);
            
            Map<String, Object> data = new HashMap<>();
            data.put("total", applicationPage.getTotalElements());
            data.put("page", page);
            data.put("size", size);
            data.put("list", applicationPage.getContent());
            
            return ApiResponse.success("获取请假申请成功", data);
        } catch (Exception e) {
            logger.error("获取请假申请失败", e);
            return ApiResponse.error(500, "获取请假申请失败: " + e.getMessage());
        }
    }

    @PostMapping("/leave/apply")
    @PreventDuplicateSubmit(interval = 3000, message = "请勿重复提交")
    public ApiResponse<LeaveApplication> applyLeave(@RequestBody LeaveApplication application) {
        logger.info("提交请假申请: employeeId={}, leaveType={}", application.getEmployeeId(), application.getLeaveType());
        try {
            LeaveApplication savedApplication = attendanceService.applyLeave(application);
            return ApiResponse.success("请假申请提交成功", savedApplication);
        } catch (Exception e) {
            logger.error("请假申请失败", e);
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PutMapping("/leave/applications/{id}/approve")
    public ApiResponse<LeaveApplication> approveLeave(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        Long approverId = request.get("approverId") != null ? Long.valueOf(request.get("approverId").toString()) : null;
        String approverName = (String) request.get("approverName");
        boolean approved = (Boolean) request.get("approved");
        String comment = (String) request.get("comment");
        
        logger.info("审批请假: id={}, approved={}", id, approved);
        try {
            LeaveApplication application = attendanceService.approveLeave(id, approverId, approverName, approved, comment);
            return ApiResponse.success("请假审批成功", application);
        } catch (Exception e) {
            logger.error("请假审批失败", e);
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @GetMapping("/leave/balance/{employeeId}")
    public ApiResponse<Map<String, BigDecimal>> getLeaveBalance(@PathVariable Long employeeId) {
        logger.info("获取假期余额: employeeId={}", employeeId);
        try {
            Map<String, BigDecimal> balance = attendanceService.getLeaveBalance(employeeId);
            return ApiResponse.success("获取假期余额成功", balance);
        } catch (Exception e) {
            logger.error("获取假期余额失败", e);
            return ApiResponse.error(500, "获取假期余额失败: " + e.getMessage());
        }
    }

    @GetMapping("/overtime/records")
    public ApiResponse<Map<String, Object>> getOvertimeRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("获取加班记录: page={}, size={}", page, size);
        try {
            Page<OvertimeRecord> recordPage = attendanceService.getOvertimeRecords(page, size);
            
            Map<String, Object> data = new HashMap<>();
            data.put("total", recordPage.getTotalElements());
            data.put("page", page);
            data.put("size", size);
            data.put("list", recordPage.getContent());
            
            return ApiResponse.success("获取加班记录成功", data);
        } catch (Exception e) {
            logger.error("获取加班记录失败", e);
            return ApiResponse.error(500, "获取加班记录失败: " + e.getMessage());
        }
    }

    @PostMapping("/overtime/apply")
    @PreventDuplicateSubmit(interval = 3000, message = "请勿重复提交")
    public ApiResponse<OvertimeRecord> applyOvertime(@RequestBody OvertimeRecord record) {
        logger.info("提交加班申请: employeeId={}", record.getEmployeeId());
        try {
            OvertimeRecord savedRecord = attendanceService.applyOvertime(record);
            return ApiResponse.success("加班申请提交成功", savedRecord);
        } catch (Exception e) {
            logger.error("加班申请失败", e);
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PutMapping("/overtime/records/{id}/approve")
    public ApiResponse<OvertimeRecord> approveOvertime(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        Long approverId = request.get("approverId") != null ? Long.valueOf(request.get("approverId").toString()) : null;
        String approverName = (String) request.get("approverName");
        boolean approved = (Boolean) request.get("approved");
        String comment = (String) request.get("comment");
        
        logger.info("审批加班: id={}, approved={}", id, approved);
        try {
            OvertimeRecord record = attendanceService.approveOvertime(id, approverId, approverName, approved, comment);
            return ApiResponse.success("加班审批成功", record);
        } catch (Exception e) {
            logger.error("加班审批失败", e);
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PostMapping("/overtime/convert")
    @PreventDuplicateSubmit(interval = 3000, message = "请勿重复提交")
    public ApiResponse<OvertimeRecord> convertOvertime(@RequestBody Map<String, Object> request) {
        Long id = Long.valueOf(request.get("id").toString());
        String conversionType = (String) request.get("conversionType");
        
        logger.info("转换加班: id={}, conversionType={}", id, conversionType);
        try {
            OvertimeRecord record = attendanceService.convertOvertime(id, conversionType);
            return ApiResponse.success("加班转换成功", record);
        } catch (Exception e) {
            logger.error("加班转换失败", e);
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @GetMapping("/statistics/monthly")
    public ApiResponse<Map<String, Object>> getMonthlyStatistics(
            @RequestParam int year,
            @RequestParam int month) {
        logger.info("获取月度统计: year={}, month={}", year, month);
        try {
            Map<String, Object> stats = attendanceService.getMonthlyStatistics(year, month);
            return ApiResponse.success("获取月度统计成功", stats);
        } catch (Exception e) {
            logger.error("获取月度统计失败", e);
            return ApiResponse.error(500, "获取月度统计失败: " + e.getMessage());
        }
    }
}
