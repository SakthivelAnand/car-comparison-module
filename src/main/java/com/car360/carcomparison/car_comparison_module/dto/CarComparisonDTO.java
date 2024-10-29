package com.car360.carcomparison.car_comparison_module.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class CarComparisonDTO {

    @NotNull(message = "User ID is required.")
    private Integer userId;

    @NotNull(message = "Base Car ID is required.")
    private Integer baseCarId;

    @NotEmpty(message = "At least one car ID must be provided for comparison.")
    private Set<Integer> compareCarIds;

    // Getters and Setters

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBaseCarId() {
        return baseCarId;
    }

    public void setBaseCarId(Integer baseCarId) {
        this.baseCarId = baseCarId;
    }

    public Set<Integer> getCompareCarIds() {
        return compareCarIds;
    }

    public void setCompareCarIds(Set<Integer> compareCarIds) {
        this.compareCarIds = compareCarIds;
    }
}

