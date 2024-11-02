package com.car360.carcomparison.car_comparison_module.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class CarComparisonDTO {

    @NotNull(message = "User ID is required.")
    private Integer userId;

    @NotNull(message = "Base Car ID is required.")
    private Integer baseCarId;

    @NotEmpty(message = "At least one car ID must be provided for comparison.")
    private Set<Integer> compareCarIds;

    public void setCompareCarIds(Set<Integer> compareCarIds) {
        this.compareCarIds = compareCarIds;
    }
}

