package com.car360.carcomparison.car_comparison_module.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ComparisonHistoryDTO {

    private Long comparisonId;
    private LocalDateTime timestamp;
    private List<CarResponseDTO> comparedCars;

    // Constructors
    public ComparisonHistoryDTO() {}

    public ComparisonHistoryDTO(Long comparisonId, LocalDateTime timestamp, List<CarResponseDTO> comparedCars) {
        this.comparisonId = comparisonId;
        this.timestamp = timestamp;
        this.comparedCars = comparedCars;
    }
}

