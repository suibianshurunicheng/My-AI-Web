package com.employee.management.repository;

import com.employee.management.entity.PerformanceScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceScoreRepository extends JpaRepository<PerformanceScore, Long> {
    List<PerformanceScore> findByCycleId(Long cycleId);
    List<PerformanceScore> findByCycleIdAndEmployeeId(Long cycleId, Long employeeId);
}
