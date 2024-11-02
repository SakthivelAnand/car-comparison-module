package com.car360.carcomparison.car_comparison_module.dto;

import lombok.Data;

import java.util.List;

/**
 * DTO representing the response of the compare API.
 */
@Data
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
}

