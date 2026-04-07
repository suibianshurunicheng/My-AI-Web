package com.employee.management.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@Data
public class Notice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "scope", nullable = false)
    private String scope; // ALL, DEPARTMENT
    
    @Column(name = "department_id")
    private Long departmentId;
    
    @Column(name = "department_name")
    private String departmentName;
    
    @Column(name = "important")
    private Boolean important;
    
    @Column(name = "send_email")
    private Boolean sendEmail;
    
    @Column(name = "send_internal_message")
    private Boolean sendInternalMessage;
    
    @Column(name = "publisher_id")
    private Long publisherId;
    
    @Column(name = "publisher_name")
    private String publisherName;
    
    @Column(name = "publish_time")
    private LocalDateTime publishTime;
    
    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (publishTime == null) {
            publishTime = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
