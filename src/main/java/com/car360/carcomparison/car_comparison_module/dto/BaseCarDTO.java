package com.car360.carcomparison.car_comparison_module.dto;

import lombok.Data;

import java.util.Map;

/**
 * DTO representing the base car details.
 */
@Data
public class BaseCarDTO {
    private Integer carId;
    private String name;
    private Map<String, String> specifications;

    // Constructors

    public BaseCarDTO() {
    }

    public BaseCarDTO(Integer carId, String name, Map<String, String> specifications) {
        this.carId = carId;
        this.name = name;
        this.specifications = specifications;
    }
}

