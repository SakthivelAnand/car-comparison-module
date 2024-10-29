package com.car360.carcomparison.car_comparison_module.service;

import com.car360.carcomparison.car_comparison_module.dto.BaseCarDTO;
import com.car360.carcomparison.car_comparison_module.dto.CompareRequestDTO;
import com.car360.carcomparison.car_comparison_module.dto.CompareResponseDTO;
import com.car360.carcomparison.car_comparison_module.dto.ComparisonDTO;
import com.car360.carcomparison.car_comparison_module.model.Car;
import com.car360.carcomparison.car_comparison_module.repository.CarComparisonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the CarComparisonService interface.
 */
@Service
public class CarComparisonServiceImpl implements CarComparisonService {

    @Autowired
    private CarService carService;

    @Autowired
    private CarComparisonRepository comparisonRepository;

    @Override
    public CompareResponseDTO compareCars(CompareRequestDTO compareRequest) {
        // Fetch base car
        Car baseCar = carService.getCarById(compareRequest.getBaseCarId());

        // Fetch compare cars
        List<Car> compareCars = carService.getCarsByIds(compareRequest.getCompareCarIds());

        // Prepare base car DTO
        BaseCarDTO baseCarDTO = new BaseCarDTO(baseCar.getCarId(), baseCar.getName());

        // Generate comparison results
        List<ComparisonDTO> comparisonDTOs = compareCars.stream()
                .map(compareCar -> {
                    // Get specifications for base car and compare car
                    Map<String, String> baseSpecs = baseCar.getCarSpecifications().stream()
                            .collect(Collectors.toMap(
                                    cs -> cs.getSpecification().getName(),
                                    cs -> cs.getValue()
                            ));

                    Map<String, String> compareSpecs = compareCar.getCarSpecifications().stream()
                            .collect(Collectors.toMap(
                                    cs -> cs.getSpecification().getName(),
                                    cs -> cs.getValue()
                            ));

                    // Determine differences
                    Map<String, String> differencesOnly = new HashMap<>();
                    if (compareRequest.getShowOnlyDifferences()) {
                        compareSpecs.forEach((key, value) -> {
                            String baseValue = baseSpecs.get(key);
                            if (!Objects.equals(baseValue, value)) {
                                differencesOnly.put(key, value);
                            }
                        });
                    }

                    return new ComparisonDTO(
                            compareCar.getCarId(),
                            compareSpecs,
                            compareRequest.getShowOnlyDifferences() ? differencesOnly : null
                    );
                })
                .collect(Collectors.toList());

        return new CompareResponseDTO(baseCarDTO, comparisonDTOs);
    }

}
