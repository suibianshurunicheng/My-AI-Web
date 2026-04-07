package com.employee.management.dto;

import lombok.Data;

@Data
public class EmployeeCountDTO {
    private Long total;
    private Long active;
    private Long inactive;

    public EmployeeCountDTO() {
    }

    public EmployeeCountDTO(Long total, Long active, Long inactive) {
        this.total = total;
        this.active = active;
        this.inactive = inactive;
    }
}
