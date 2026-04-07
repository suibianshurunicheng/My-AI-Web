package com.employee.management.repository;

import com.employee.management.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    
    List<Job> findByDepartmentId(Long departmentId);
    
    List<Job> findByPositionId(Long positionId);
    
    List<Job> findByStatus(String status);
    
    List<Job> findByDepartmentIdAndStatus(Long departmentId, String status);
    
    List<Job> findByPositionIdAndStatus(Long positionId, String status);
    
    List<Job> findByDepartmentIdAndPositionId(Long departmentId, Long positionId);
    
    List<Job> findByDepartmentIdAndPositionIdAndStatus(Long departmentId, Long positionId, String status);
}