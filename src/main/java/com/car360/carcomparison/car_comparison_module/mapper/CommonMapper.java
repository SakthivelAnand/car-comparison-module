package com.car360.carcomparison.car_comparison_module.mapper;

import com.car360.carcomparison.car_comparison_module.dto.CarResponseDTO;
import com.car360.carcomparison.car_comparison_module.dto.CarSpecificationDTO;
import com.car360.carcomparison.car_comparison_module.dto.ComparisonHistoryDTO;
import com.car360.carcomparison.car_comparison_module.model.Car;
import com.car360.carcomparison.car_comparison_module.model.CarSpecification;
import com.car360.carcomparison.car_comparison_module.model.Comparison;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommonMapper {

    // Map Comparison entity to ComparisonHistoryDTO
    public ComparisonHistoryDTO toComparisonHistoryDTO(Comparison comparison) {
        if (comparison == null) {
            return null;
        }

        List<CarResponseDTO> comparedCars = comparison.getComparisonItems().stream()
                .map(comparisonItem -> toCarResponseDTO(comparisonItem.getCar()))
                .collect(Collectors.toList());

        return new ComparisonHistoryDTO(
                comparison.getId(),
                comparison.getTimestamp(),
                comparedCars
        );
    }

    // Map Car entity to CarResponseDTO
    public CarResponseDTO toCarResponseDTO(Car car) {
        if (car == null) {
            return null;
        }

        List<CarSpecificationDTO> carSpecifications = car.getCarSpecifications().stream()
                .map(this::toCarSpecificationDTO)
                .collect(Collectors.toList());

        return new CarResponseDTO(
                car.getCarId(),
                car.getName(),
                car.getBrand(),
                car.getModelYear(),
                car.getCategory(),
                car.getPriceRange(),
                car.getImageUrl(),
                car.getDescription(),
                carSpecifications
        );
    }

    // Map CarSpecification entity to CarSpecificationDTO
    public CarSpecificationDTO toCarSpecificationDTO(CarSpecification carSpecification) {
        if (carSpecification == null || carSpecification.getSpecification() == null) {
            return null;
        }

        return new CarSpecificationDTO(
                carSpecification.getSpecification().getName(),
                carSpecification.getValue()
        );
    }

    // Map a list of Comparison entities to a list of ComparisonHistoryDTOs
    public List<ComparisonHistoryDTO> toComparisonHistoryDTOs(List<Comparison> comparisons) {
        return comparisons.stream()
                .map(this::toComparisonHistoryDTO)
                .collect(Collectors.toList());
    }
}

