package com.car360.carcomparison.car_comparison_module.dto;

import java.util.Map;

/**
 * DTO representing the comparison details for a single car.
 */
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

    // Getters and Setters

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public Map<String, String> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Map<String, String> specifications) {
        this.specifications = specifications;
    }

    public Map<String, String> getDifferencesOnly() {
        return differencesOnly;
    }

    public void setDifferencesOnly(Map<String, String> differencesOnly) {
        this.differencesOnly = differencesOnly;
    }
}

