package com.employee.management.repository;

import com.employee.management.entity.PerformanceCycle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceCycleRepository extends JpaRepository<PerformanceCycle, Long> {
    Page<PerformanceCycle> findByCycleType(String cycleType, Pageable pageable);
    List<PerformanceCycle> findByStatus(String status);
}
