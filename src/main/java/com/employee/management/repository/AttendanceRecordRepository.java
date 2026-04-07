package com.employee.management.repository;

import com.employee.management.entity.AttendanceRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {

    Optional<AttendanceRecord> findByEmployeeIdAndAttendanceDate(Long employeeId, LocalDate attendanceDate);

    Page<AttendanceRecord> findByEmployeeId(Long employeeId, Pageable pageable);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.attendanceDate BETWEEN :startDate AND :endDate")
    List<AttendanceRecord> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.employeeId = :employeeId AND a.attendanceDate BETWEEN :startDate AND :endDate")
    List<AttendanceRecord> findByEmployeeIdAndDateRange(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(a) FROM AttendanceRecord a WHERE a.employeeId = :employeeId AND a.status = :status AND a.attendanceDate BETWEEN :startDate AND :endDate")
    long countByEmployeeIdAndStatusAndDateRange(
            @Param("employeeId") Long employeeId,
            @Param("status") String status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
