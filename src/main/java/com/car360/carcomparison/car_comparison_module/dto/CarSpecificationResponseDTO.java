package com.car360.carcomparison.car_comparison_module.dto;

import lombok.Data;

@Data
public class CarSpecificationResponseDTO {

    private Integer carId;
    private Integer specId;
    private String name;
    private String value;

    // Constructors
    public CarSpecificationResponseDTO() {}

    public CarSpecificationResponseDTO(Integer carId, Integer specId, String name, String value) {
        this.carId = carId;
        this.specId = specId;
        this.name = name;
        this.value = value;
    }
}
