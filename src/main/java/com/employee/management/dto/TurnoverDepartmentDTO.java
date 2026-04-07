package com.employee.management.dto;

import lombok.Data;

@Data
public class TurnoverDepartmentDTO {
    private String departmentName;
    private Long totalEmployees;
    private Long departures;
    private Double turnoverRate;

    public TurnoverDepartmentDTO() {
    }

    public TurnoverDepartmentDTO(String departmentName, Long totalEmployees, Long departures, Double turnoverRate) {
        this.departmentName = departmentName;
        this.totalEmployees = totalEmployees;
        this.departures = departures;
        this.turnoverRate = turnoverRate;
    }
}
