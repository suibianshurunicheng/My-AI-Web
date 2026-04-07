package com.employee.management.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "recruitment_interviews")
public class Interview {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "resume_id", nullable = false)
    private Long resumeId;
    
    @Column(name = "candidate_name")
    private String candidateName;
    
    @Column(name = "job_title")
    private String jobTitle;
    
    @Column(name = "interview_time")
    private LocalDateTime interviewTime;
    
    @Column(name = "interviewers")
    private String interviewers;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "status")
    private String status; // SCHEDULED, COMPLETED
    
    @Column(name = "evaluation")
    private String evaluation;
    
    @Column(name = "result")
    private String result; // PASSED, REJECTED
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = "SCHEDULED";
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}