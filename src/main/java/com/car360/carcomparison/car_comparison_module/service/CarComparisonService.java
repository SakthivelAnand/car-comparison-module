package com.car360.carcomparison.car_comparison_module.service;


import com.car360.carcomparison.car_comparison_module.dto.CompareRequestDTO;
import com.car360.carcomparison.car_comparison_module.dto.CompareResponseDTO;

/**
 * Service interface for CarComparison-related operations.
 */
public interface CarComparisonService {
    CompareResponseDTO compareCars(CompareRequestDTO compareRequest);
}


