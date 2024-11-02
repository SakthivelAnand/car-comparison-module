package com.car360.carcomparison.car_comparison_module.dto;

import lombok.Data;

import java.util.Map;

/**
 * DTO representing the comparison details for a single car.
 */
@Data
public class ComparisonDTO {
    private Integer carId;
    private Map<String, String> specifications;
    private Map<String, String> differencesOnly;

    // Constructors

    public ComparisonDTO() {
    }

    public ComparisonDTO(Integer carId, Map<String, String> specifications, Map<String, String> differencesOnly) {
        this.carId = carId;
        this.specifications = specifications;
        this.differencesOnly = differencesOnly;
    }
}

