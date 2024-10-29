package com.car360.carcomparison.car_comparison_module.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * DTO for handling the compare request.
 */
public class CompareRequestDTO {

    @NotNull(message = "Base Car ID is required.")
    private Integer baseCarId;

    @NotEmpty(message = "At least one Compare Car ID is required.")
    private List<Integer> compareCarIds;

    @NotNull(message = "show_only_differences flag is required.")
    private Boolean showOnlyDifferences;

    // Getters and Setters

    public Integer getBaseCarId() {
        return baseCarId;
    }

    public void setBaseCarId(Integer baseCarId) {
        this.baseCarId = baseCarId;
    }

    public List<Integer> getCompareCarIds() {
        return compareCarIds;
    }

    public void setCompareCarIds(List<Integer> compareCarIds) {
        this.compareCarIds = compareCarIds;
    }

    public Boolean getShowOnlyDifferences() {
        return showOnlyDifferences;
    }

    public void setShowOnlyDifferences(Boolean showOnlyDifferences) {
        this.showOnlyDifferences = showOnlyDifferences;
    }
}

