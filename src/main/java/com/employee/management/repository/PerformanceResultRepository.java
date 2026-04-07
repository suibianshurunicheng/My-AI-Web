package com.employee.management.repository;

import com.employee.management.entity.PerformanceResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceResultRepository extends JpaRepository<PerformanceResult, Long> {
    Page<PerformanceResult> findByCycleId(Long cycleId, Pageable pageable);
    List<PerformanceResult> findByCycleId(Long cycleId);
    List<PerformanceResult> findByEmployeeId(Long employeeId);
    List<PerformanceResult> findByStatus(String status);
}
