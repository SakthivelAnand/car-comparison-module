package com.car360.carcomparison.car_comparison_module.service;

import com.car360.carcomparison.car_comparison_module.dto.*;
import com.car360.carcomparison.car_comparison_module.exception.ResourceNotFoundException;
import com.car360.carcomparison.car_comparison_module.mapper.CommonMapper;
import com.car360.carcomparison.car_comparison_module.model.Car;
import com.car360.carcomparison.car_comparison_module.model.Comparison;
import com.car360.carcomparison.car_comparison_module.model.ComparisonItem;
import com.car360.carcomparison.car_comparison_module.model.User;
import com.car360.carcomparison.car_comparison_module.repository.CarRepository;
import com.car360.carcomparison.car_comparison_module.repository.ComparisonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private ComparisonRepository comparisonRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CommonMapper commonMapper;


    @Override
    public CompareResponseDTO compareCars(CompareRequestDTO compareRequest) {
        // Fetch base car
        Car baseCar = carService.getCarById(compareRequest.getBaseCarId());

        // Fetch compare cars
        List<Car> compareCars = carService.getCarsByIds(compareRequest.getCompareCarIds());


        // Get specifications for base car and compare car
        Map<String, String> baseSpecs = baseCar.getCarSpecifications().stream()
                .collect(Collectors.toMap(
                        cs -> cs.getSpecification().getName(),
                        cs -> cs.getValue()
                ));

        // Prepare base car DTO
        BaseCarDTO baseCarDTO = new BaseCarDTO(baseCar.getCarId(), baseCar.getName(), baseSpecs);

        // Generate comparison results
        List<ComparisonDTO> comparisonDTOs = compareCars.stream()
                .map(compareCar -> {

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

    @Override
    @Transactional
    public Comparison saveComparison(User user, List<Integer> carIds) {
        // Fetch cars by IDs
        List<Car> cars = carRepository.findAllById(carIds);
        if (cars.size() != carIds.size()) {
            List<Integer> foundIds = cars.stream().map(Car::getCarId).collect(Collectors.toList());
            List<Integer> missingIds = carIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toList());
            throw new ResourceNotFoundException("Cars not found with IDs: " + missingIds);
        }

        // Create a new Comparison
        Comparison comparison = new Comparison();
        comparison.setUser(user);
        comparison.setTimestamp(LocalDateTime.now());

        // Create ComparisonItems
        List<ComparisonItem> comparisonItems = cars.stream()
                .map(car -> new ComparisonItem(comparison, car))
                .collect(Collectors.toList());

        comparison.setComparisonItems(new java.util.HashSet<>(comparisonItems));

        // Save Comparison along with ComparisonItems
        return comparisonRepository.save(comparison);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComparisonHistoryDTO> getComparisonHistory(User user) {
        List<Comparison> comparisons = comparisonRepository.findByUserOrderByTimestampDesc(user);
        return commonMapper.toComparisonHistoryDTOs(comparisons);
    }

}
