package com.car360.carcomparison.car_comparison_module.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * DTO for handling the compare request.
 */
@Data
public class CompareRequestDTO {

    @NotNull(message = "Base Car ID is required.")
    private Integer baseCarId;

    @NotEmpty(message = "At least one Compare Car ID is required.")
    private List<Integer> compareCarIds;

    @NotNull(message = "show_only_differences flag is required.")
    private Boolean showOnlyDifferences;

    @NotNull(message = "User who requested the compare request")
    private Long userId;

}

