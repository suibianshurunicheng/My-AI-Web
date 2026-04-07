package com.employee.management.repository;

import com.employee.management.entity.LeaveApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {

    Page<LeaveApplication> findByEmployeeId(Long employeeId, Pageable pageable);

    Page<LeaveApplication> findByStatus(String status, Pageable pageable);

    @Query("SELECT l FROM LeaveApplication l WHERE l.employeeId = :employeeId AND l.status = 'APPROVED' AND l.startDate BETWEEN :startDate AND :endDate")
    List<LeaveApplication> findApprovedLeavesByEmployeeIdAndDateRange(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(l.days), 0) FROM LeaveApplication l WHERE l.employeeId = :employeeId AND l.leaveType = :leaveType AND l.status = 'APPROVED'")
    BigDecimal sumUsedDaysByEmployeeIdAndLeaveType(
            @Param("employeeId") Long employeeId,
            @Param("leaveType") String leaveType);
}
