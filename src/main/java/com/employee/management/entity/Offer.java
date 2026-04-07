package com.employee.management.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "recruitment_offers")
public class Offer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "resume_id", nullable = false)
    private Long resumeId;
    
    @Column(name = "candidate_name")
    private String candidateName;
    
    @Column(name = "job_id")
    private Long jobId;
    
    @Column(name = "job_title")
    private String jobTitle;
    
    @Column(name = "salary")
    private String salary;
    
    @Column(name = "join_date")
    private LocalDate joinDate;
    
    @Column(name = "status")
    private String status; // PENDING, ACCEPTED, REJECTED
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = "PENDING";
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}