package com.employee.management.repository;

import com.employee.management.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    
    List<Resume> findByJobId(Long jobId);
    
    List<Resume> findByStatus(String status);
    
    List<Resume> findByJobIdAndStatus(Long jobId, String status);
}