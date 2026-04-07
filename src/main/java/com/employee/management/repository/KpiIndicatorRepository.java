package com.employee.management.repository;

import com.employee.management.entity.KpiIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KpiIndicatorRepository extends JpaRepository<KpiIndicator, Long> {
    List<KpiIndicator> findByIsActiveTrue();
    List<KpiIndicator> findByPosition(String position);
    List<KpiIndicator> findByPositionAndIsActiveTrue(String position);
}
