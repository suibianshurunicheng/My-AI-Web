package com.employee.management.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "performance_cycles")
public class PerformanceCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "cycle_type", nullable = false)
    private String cycleType;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "self_eval_start")
    private LocalDateTime selfEvalStart;

    @Column(name = "self_eval_end")
    private LocalDateTime selfEvalEnd;

    @Column(name = "super_eval_start")
    private LocalDateTime superEvalStart;

    @Column(name = "super_eval_end")
    private LocalDateTime superEvalEnd;

    @Column(name = "status", nullable = false)
    private String status = "DRAFT";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getSelfEvalStart() {
        return selfEvalStart;
    }

    public void setSelfEvalStart(LocalDateTime selfEvalStart) {
        this.selfEvalStart = selfEvalStart;
    }

    public LocalDateTime getSelfEvalEnd() {
        return selfEvalEnd;
    }

    public void setSelfEvalEnd(LocalDateTime selfEvalEnd) {
        this.selfEvalEnd = selfEvalEnd;
    }

    public LocalDateTime getSuperEvalStart() {
        return superEvalStart;
    }

    public void setSuperEvalStart(LocalDateTime superEvalStart) {
        this.superEvalStart = superEvalStart;
    }

    public LocalDateTime getSuperEvalEnd() {
        return superEvalEnd;
    }

    public void setSuperEvalEnd(LocalDateTime superEvalEnd) {
        this.superEvalEnd = superEvalEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
