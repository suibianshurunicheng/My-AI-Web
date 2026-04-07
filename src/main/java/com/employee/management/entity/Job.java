package com.employee.management.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "recruitment_jobs")
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "department_id", nullable = false)
    private Long departmentId;
    
    @Column(name = "department_name")
    private String departmentName;
    
    @Column(name = "position_id", nullable = false)
    private Long positionId;
    
    @Column(name = "position_name")
    private String positionName;
    
    @Column(name = "requirement")
    private String requirement;
    
    @Column(name = "salary_range")
    private String salaryRange;
    
    @Column(name = "recruit_count")
    private Integer recruitCount;
    
    @Column(name = "status")
    private String status; // ACTIVE, INACTIVE
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}