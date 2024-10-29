package com.car360.carcomparison.car_comparison_module.dto;

import java.util.List;

/**
 * DTO representing the response of the compare API.
 */
public class CompareResponseDTO {
    private BaseCarDTO baseCar;
    private List<ComparisonDTO> comparisons;

    // Constructors

    public CompareResponseDTO() {
    }

    public CompareResponseDTO(BaseCarDTO baseCar, List<ComparisonDTO> comparisons) {
        this.baseCar = baseCar;
        this.comparisons = comparisons;
    }

    // Getters and Setters

    public BaseCarDTO getBaseCar() {
        return baseCar;
    }

    public void setBaseCar(BaseCarDTO baseCar) {
        this.baseCar = baseCar;
    }

    public List<ComparisonDTO> getComparisons() {
        return comparisons;
    }

    public void setComparisons(List<ComparisonDTO> comparisons) {
        this.comparisons = comparisons;
    }
}

