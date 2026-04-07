package com.employee.management.dto;

import lombok.Data;

@Data
public class GenderRatioDTO {
    private String gender;
    private Long count;
    private Double percentage;

    public GenderRatioDTO() {
    }

    public GenderRatioDTO(String gender, Long count, Double percentage) {
        this.gender = gender;
        this.count = count;
        this.percentage = percentage;
    }
}
