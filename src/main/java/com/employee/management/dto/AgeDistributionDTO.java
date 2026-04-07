package com.employee.management.dto;

import lombok.Data;

@Data
public class AgeDistributionDTO {
    private String range;
    private Long count;

    public AgeDistributionDTO() {
    }

    public AgeDistributionDTO(String range, Long count) {
        this.range = range;
        this.count = count;
    }
}
