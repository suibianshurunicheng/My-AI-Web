package com.employee.management.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "performance_scores")
public class PerformanceScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cycle_id", nullable = false)
    private Long cycleId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    @Column(name = "employee_code", nullable = false)
    private String employeeCode;

    @Column(name = "kpi_indicator_id", nullable = false)
    private Long kpiIndicatorId;

    @Column(name = "kpi_indicator_name", nullable = false)
    private String kpiIndicatorName;

    @Column(name = "kpi_weight", nullable = false, precision = 10, scale = 2)
    private BigDecimal kpiWeight;

    @Column(name = "self_score", precision = 10, scale = 2)
    private BigDecimal selfScore;

    @Column(name = "self_comment", length = 1000)
    private String selfComment;

    @Column(name = "supervisor_score", precision = 10, scale = 2)
    private BigDecimal supervisorScore;

    @Column(name = "supervisor_comment", length = 1000)
    private String supervisorComment;

    @Column(name = "final_score", precision = 10, scale = 2)
    private BigDecimal finalScore;

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

    public Long getCycleId() {
        return cycleId;
    }

    public void setCycleId(Long cycleId) {
        this.cycleId = cycleId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public Long getKpiIndicatorId() {
        return kpiIndicatorId;
    }

    public void setKpiIndicatorId(Long kpiIndicatorId) {
        this.kpiIndicatorId = kpiIndicatorId;
    }

    public String getKpiIndicatorName() {
        return kpiIndicatorName;
    }

    public void setKpiIndicatorName(String kpiIndicatorName) {
        this.kpiIndicatorName = kpiIndicatorName;
    }

    public BigDecimal getKpiWeight() {
        return kpiWeight;
    }

    public void setKpiWeight(BigDecimal kpiWeight) {
        this.kpiWeight = kpiWeight;
    }

    public BigDecimal getSelfScore() {
        return selfScore;
    }

    public void setSelfScore(BigDecimal selfScore) {
        this.selfScore = selfScore;
    }

    public String getSelfComment() {
        return selfComment;
    }

    public void setSelfComment(String selfComment) {
        this.selfComment = selfComment;
    }

    public BigDecimal getSupervisorScore() {
        return supervisorScore;
    }

    public void setSupervisorScore(BigDecimal supervisorScore) {
        this.supervisorScore = supervisorScore;
    }

    public String getSupervisorComment() {
        return supervisorComment;
    }

    public void setSupervisorComment(String supervisorComment) {
        this.supervisorComment = supervisorComment;
    }

    public BigDecimal getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(BigDecimal finalScore) {
        this.finalScore = finalScore;
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
