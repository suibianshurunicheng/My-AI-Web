package com.employee.management.repository;

import com.employee.management.entity.OvertimeRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OvertimeRecordRepository extends JpaRepository<OvertimeRecord, Long> {

    Page<OvertimeRecord> findByEmployeeId(Long employeeId, Pageable pageable);

    Page<OvertimeRecord> findByStatus(String status, Pageable pageable);

    @Query("SELECT o FROM OvertimeRecord o WHERE o.employeeId = :employeeId AND o.status = 'APPROVED' AND o.startTime BETWEEN :startTime AND :endTime")
    List<OvertimeRecord> findApprovedOvertimeByEmployeeIdAndDateRange(
            @Param("employeeId") Long employeeId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query("SELECT COALESCE(SUM(o.compensatoryDays), 0) FROM OvertimeRecord o WHERE o.employeeId = :employeeId AND o.conversionType = 'COMPENSATORY_LEAVE' AND o.status = 'CONVERTED'")
    BigDecimal sumCompensatoryDaysByEmployeeId(
            @Param("employeeId") Long employeeId);
}
