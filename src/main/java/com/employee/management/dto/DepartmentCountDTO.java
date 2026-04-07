package com.employee.management.dto;

import lombok.Data;

@Data
public class DepartmentCountDTO {
    private String departmentName;
    private Long count;

    public DepartmentCountDTO() {
    }

    public DepartmentCountDTO(String departmentName, Long count) {
        this.departmentName = departmentName;
        this.count = count;
    }
}
