package com.employee.management.repository;

import com.employee.management.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    
    List<Interview> findByResumeId(Long resumeId);
    
    List<Interview> findByStatus(String status);
    
    List<Interview> findByResumeIdAndStatus(Long resumeId, String status);
}