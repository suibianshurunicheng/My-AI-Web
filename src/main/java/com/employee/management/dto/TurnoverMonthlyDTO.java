package com.employee.management.dto;

import lombok.Data;

@Data
public class TurnoverMonthlyDTO {
    private Integer month;
    private Long totalEmployees;
    private Long departures;
    private Double turnoverRate;

    public TurnoverMonthlyDTO() {
    }

    public TurnoverMonthlyDTO(Integer month, Long totalEmployees, Long departures, Double turnoverRate) {
        this.month = month;
        this.totalEmployees = totalEmployees;
        this.departures = departures;
        this.turnoverRate = turnoverRate;
    }
}
