package com.car360.carcomparison.car_comparison_module.service;


import com.car360.carcomparison.car_comparison_module.dto.CompareRequestDTO;
import com.car360.carcomparison.car_comparison_module.dto.CompareResponseDTO;
import com.car360.carcomparison.car_comparison_module.model.Comparison;
import com.car360.carcomparison.car_comparison_module.model.User;

import java.util.List;

/**
 * Service interface for CarComparison-related operations.
 */
public interface CarComparisonService {
    CompareResponseDTO compareCars(CompareRequestDTO compareRequest);
    Comparison saveComparison(User user, List<Integer> carIds);
    List<Comparison> getComparisonHistory(User user);
}


