package com.employee.management.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
@Data
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "员工姓名不能为空")
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    
    @NotBlank(message = "性别不能为空")
    @Column(name = "gender", nullable = false, length = 10)
    private String gender;
    
    @NotNull(message = "年龄不能为空")
    @Column(name = "age", nullable = false)
    private Integer age;
    
    @NotNull(message = "出生日期不能为空")
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    
    @NotNull(message = "身高不能为空")
    @Column(name = "height", nullable = false)
    private Double height;
    
    @NotNull(message = "体重不能为空")
    @Column(name = "weight", nullable = false)
    private Double weight;
    
    @NotBlank(message = "职位不能为空")
    @Column(name = "position", nullable = false, length = 100)
    private String position;
    
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    @NotBlank(message = "电话号码不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;
    
    @NotBlank(message = "员工编码不能为空")
    @Column(name = "employee_code", nullable = false, unique = true, length = 20)
    private String employeeCode;
    
    @Column(name = "department_id")
    private Long departmentId;
    
    @Column(name = "department_name", length = 100)
    private String departmentName;
    
    @Column(name = "position_id")
    private Long positionId;
    
    @Column(name = "position_name", length = 100)
    private String positionName;
    
    @Column(name = "entry_date")
    private LocalDate entryDate;
    
    @Column(name = "status", length = 20)
    private String status;
    
    @Column(name = "created_at", updatable = false)
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
}