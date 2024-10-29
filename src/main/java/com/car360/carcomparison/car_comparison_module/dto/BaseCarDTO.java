package com.car360.carcomparison.car_comparison_module.dto;

/**
 * DTO representing the base car details.
 */
public class BaseCarDTO {
    private Integer carId;
    private String name;

    // Constructors

    public BaseCarDTO() {
    }

    public BaseCarDTO(Integer carId, String name) {
        this.carId = carId;
        this.name = name;
    }

    // Getters and Setters

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

