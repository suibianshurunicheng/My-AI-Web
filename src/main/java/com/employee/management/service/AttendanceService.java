package com.employee.management.service;

import com.employee.management.entity.AttendanceRecord;
import com.employee.management.entity.LeaveApplication;
import com.employee.management.entity.OvertimeRecord;
import com.employee.management.repository.AttendanceRecordRepository;
import com.employee.management.repository.LeaveApplicationRepository;
import com.employee.management.repository.OvertimeRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceService {

    private static final LocalTime STANDARD_CHECK_IN_TIME = LocalTime.of(9, 0);
    private static final LocalTime STANDARD_CHECK_OUT_TIME = LocalTime.of(18, 0);

    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private OvertimeRecordRepository overtimeRecordRepository;

    public Page<AttendanceRecord> getAttendanceRecords(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "attendanceDate"));
        return attendanceRecordRepository.findAll(pageable);
    }

    public AttendanceRecord checkIn(Long employeeId, String employeeName) {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        AttendanceRecord record = attendanceRecordRepository
                .findByEmployeeIdAndAttendanceDate(employeeId, today)
                .orElse(new AttendanceRecord());

        if (record.getCheckInTime() != null) {
            throw new RuntimeException("今天已经打过上班卡了");
        }

        record.setEmployeeId(employeeId);
        record.setEmployeeName(employeeName);
        record.setAttendanceDate(today);
        record.setCheckInTime(now);

        String status = "NORMAL";
        if (now.toLocalTime().isAfter(STANDARD_CHECK_IN_TIME)) {
            status = "LATE";
        }
        record.setStatus(status);

        return attendanceRecordRepository.save(record);
    }

    public AttendanceRecord checkOut(Long employeeId, String employeeName) {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        AttendanceRecord record = attendanceRecordRepository
                .findByEmployeeIdAndAttendanceDate(employeeId, today)
                .orElseThrow(() -> new RuntimeException("请先打上班卡"));

        if (record.getCheckOutTime() != null) {
            throw new RuntimeException("今天已经打过下班卡了");
        }

        record.setCheckOutTime(now);

        if (!record.getStatus().equals("LATE") && now.toLocalTime().isBefore(STANDARD_CHECK_OUT_TIME)) {
            record.setStatus("EARLY_LEAVE");
        }

        return attendanceRecordRepository.save(record);
    }

    public AttendanceRecord applyMakeup(Long employeeId, String employeeName, LocalDate date, String type, String reason) {
        AttendanceRecord record = attendanceRecordRepository
                .findByEmployeeIdAndAttendanceDate(employeeId, date)
                .orElse(new AttendanceRecord());

        record.setEmployeeId(employeeId);
        record.setEmployeeName(employeeName);
        record.setAttendanceDate(date);
        record.setIsMakeup(true);
        record.setMakeupStatus("PENDING");
        record.setMakeupReason(reason);

        if ("CHECK_IN".equals(type)) {
            LocalDateTime checkInTime = date.atTime(STANDARD_CHECK_IN_TIME);
            record.setCheckInTime(checkInTime);
        } else if ("CHECK_OUT".equals(type)) {
            LocalDateTime checkOutTime = date.atTime(STANDARD_CHECK_OUT_TIME);
            record.setCheckOutTime(checkOutTime);
        }

        return attendanceRecordRepository.save(record);
    }

    public AttendanceRecord approveMakeup(Long id, boolean approved, String comment) {
        AttendanceRecord record = attendanceRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("补卡记录不存在"));

        record.setMakeupStatus(approved ? "APPROVED" : "REJECTED");
        if (approved) {
            record.setStatus("NORMAL");
        }

        return attendanceRecordRepository.save(record);
    }

    public LeaveApplication applyLeave(LeaveApplication application) {
        if (application.getStartDate().isAfter(application.getEndDate())) {
            throw new RuntimeException("开始日期不能晚于结束日期");
        }

        long daysBetween = Duration.between(
                application.getStartDate().atStartOfDay(),
                application.getEndDate().atStartOfDay()
        ).toDays() + 1;
        application.setDays(BigDecimal.valueOf(daysBetween));

        return leaveApplicationRepository.save(application);
    }

    public LeaveApplication approveLeave(Long id, Long approverId, String approverName, boolean approved, String comment) {
        LeaveApplication application = leaveApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("请假申请不存在"));

        application.setStatus(approved ? "APPROVED" : "REJECTED");
        application.setApproverId(approverId);
        application.setApproverName(approverName);
        application.setApprovalComment(comment);
        application.setApprovedAt(LocalDateTime.now());

        return leaveApplicationRepository.save(application);
    }

    public Page<LeaveApplication> getLeaveApplications(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return leaveApplicationRepository.findAll(pageable);
    }

    public Map<String, BigDecimal> getLeaveBalance(Long employeeId) {
        Map<String, BigDecimal> balance = new HashMap<>();
        balance.put("ANNUAL", new BigDecimal("10").subtract(
                leaveApplicationRepository.sumUsedDaysByEmployeeIdAndLeaveType(employeeId, "ANNUAL")
        ));
        balance.put("SICK", new BigDecimal("5").subtract(
                leaveApplicationRepository.sumUsedDaysByEmployeeIdAndLeaveType(employeeId, "SICK")
        ));
        return balance;
    }

    public OvertimeRecord applyOvertime(OvertimeRecord record) {
        if (record.getStartTime().isAfter(record.getEndTime())) {
            throw new RuntimeException("开始时间不能晚于结束时间");
        }

        Duration duration = Duration.between(record.getStartTime(), record.getEndTime());
        BigDecimal hours = BigDecimal.valueOf(duration.toMinutes())
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        record.setHours(hours);

        return overtimeRecordRepository.save(record);
    }

    public OvertimeRecord approveOvertime(Long id, Long approverId, String approverName, boolean approved, String comment) {
        OvertimeRecord record = overtimeRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("加班记录不存在"));

        record.setStatus(approved ? "APPROVED" : "REJECTED");
        record.setApproverId(approverId);
        record.setApproverName(approverName);
        record.setApprovalComment(comment);
        record.setApprovedAt(LocalDateTime.now());

        return overtimeRecordRepository.save(record);
    }

    public OvertimeRecord convertOvertime(Long id, String conversionType) {
        OvertimeRecord record = overtimeRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("加班记录不存在"));

        if (!"APPROVED".equals(record.getStatus())) {
            throw new RuntimeException("只有已批准的加班才能转换");
        }

        record.setConversionType(conversionType);
        record.setConvertedAt(LocalDateTime.now());

        if ("COMPENSATORY_LEAVE".equals(conversionType)) {
            BigDecimal days = record.getHours().divide(BigDecimal.valueOf(8), 1, RoundingMode.HALF_UP);
            record.setCompensatoryDays(days);
        } else if ("OVERTIME_PAY".equals(conversionType)) {
            BigDecimal pay = record.getHours().multiply(new BigDecimal("50"));
            record.setOvertimePay(pay);
        }

        return overtimeRecordRepository.save(record);
    }

    public Page<OvertimeRecord> getOvertimeRecords(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return overtimeRecordRepository.findAll(pageable);
    }

    public Map<String, Object> getMonthlyStatistics(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        Map<String, Object> stats = new HashMap<>();
        stats.put("year", year);
        stats.put("month", month);
        stats.put("totalDays", endDate.getDayOfMonth());

        List<AttendanceRecord> records = attendanceRecordRepository.findByDateRange(startDate, endDate);

        long normalDays = records.stream().filter(r -> "NORMAL".equals(r.getStatus())).count();
        long lateDays = records.stream().filter(r -> "LATE".equals(r.getStatus())).count();
        long earlyLeaveDays = records.stream().filter(r -> "EARLY_LEAVE".equals(r.getStatus())).count();

        stats.put("normalDays", normalDays);
        stats.put("lateDays", lateDays);
        stats.put("earlyLeaveDays", earlyLeaveDays);
        stats.put("absentDays", endDate.getDayOfMonth() - normalDays - lateDays - earlyLeaveDays);

        return stats;
    }
}
