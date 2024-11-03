package com.car360.carcomparison.car_comparison_module.utils;

import com.car360.carcomparison.car_comparison_module.model.Car;
import com.car360.carcomparison.car_comparison_module.model.CarSpecification;
import com.car360.carcomparison.car_comparison_module.repository.CarRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for determining similar cars based on specified criteria.
 */
@Component
public class SimilarityUtil {

    @Autowired
    private CarRepository carRepository;

    public List<Car> findSimilarCars(Car baseCar, int limit) {
        Hibernate.initialize(baseCar.getCarSpecifications());
        baseCar.getCarSpecifications().forEach(spec -> Hibernate.initialize(spec.getSpecification()));
        String brand = baseCar.getBrand();
        String category = baseCar.getCategory();
        Integer priceRange = baseCar.getPriceRange();
        Integer modelYear = baseCar.getModelYear();
        List<CarSpecification> baseSpecifications = baseCar.getCarSpecifications(); // Get base car specifications

        // Similarity criteria thresholds
        int priceRangeTolerance = 1; // ±1
        int modelYearTolerance = 1;   // ±1

        // Fetch cars that match the brand and category, and are within the specified price range and model year
        List<Car> candidateCars = carRepository.findAll().stream()
                .filter(car -> !car.getCarId().equals(baseCar.getCarId())) // Exclude the base car
                .filter(car -> car.getBrand().equalsIgnoreCase(brand))     // Same brand
                .filter(car -> car.getCategory().equalsIgnoreCase(category)) // Same category
                .filter(car -> Math.abs(car.getPriceRange() - priceRange) <= priceRangeTolerance) // Similar price range
                .filter(car -> Math.abs(car.getModelYear() - modelYear) <= modelYearTolerance)     // Similar model year
                .collect(Collectors.toList());

        // If not enough candidates, relax the criteria by removing model year tolerance
        if (candidateCars.size() < limit) {
            List<Car> additionalCars = carRepository.findAll().stream()
                    .filter(car -> !car.getCarId().equals(baseCar.getCarId()))
                    .filter(car -> car.getBrand().equalsIgnoreCase(brand))
                    .filter(car -> car.getCategory().equalsIgnoreCase(category))
                    .filter(car -> Math.abs(car.getPriceRange() - priceRange) <= priceRangeTolerance)
                    .collect(Collectors.toList());
            // Remove already included cars to avoid duplicates
            additionalCars.removeAll(candidateCars);
            candidateCars.addAll(additionalCars);
        }

        // Assign similarity scores based on the number of matching criteria and specifications
        List<Car> similarCars = candidateCars.stream()
                .map(car -> new CarSimilarity(car, calculateOverallSimilarityScore(baseCar, car, baseSpecifications)))
                .sorted(Comparator.comparingInt(CarSimilarity::getSimilarityScore).reversed()) // Sort by highest similarity
                .limit(limit) // Limit to the specified number
                .map(CarSimilarity::getCar)
                .collect(Collectors.toList());

        return similarCars;
    }

    // Method to calculate similarity score based on specifications
    private int calculateSpecificationSimilarityScore(List<CarSpecification> baseSpecifications, List<CarSpecification> candidateSpecifications) {
        int matchingSpecifications = 0;

        for (CarSpecification baseSpec : baseSpecifications) {
            for (CarSpecification candidateSpec : candidateSpecifications) {
                if (baseSpec.getSpecification().getSpecId().equals(candidateSpec.getSpecification().getSpecId()) &&
                        baseSpec.getValue().equalsIgnoreCase(candidateSpec.getValue())) {
                    matchingSpecifications++;
                    break;
                }
            }
        }
        return matchingSpecifications;
    }

    // Method to calculate the overall similarity score combining general attributes and specification match
    private int calculateOverallSimilarityScore(Car baseCar, Car candidateCar, List<CarSpecification> baseSpecifications) {
        int score = 0;

        // General criteria similarity
        if (candidateCar.getBrand().equalsIgnoreCase(baseCar.getBrand())) score++;
        if (candidateCar.getCategory().equalsIgnoreCase(baseCar.getCategory())) score++;
        if (Math.abs(candidateCar.getPriceRange() - baseCar.getPriceRange()) <= 1) score++;
        if (Math.abs(candidateCar.getModelYear() - baseCar.getModelYear()) <= 1) score++;

        // Add specification similarity score
        score += calculateSpecificationSimilarityScore(baseSpecifications, candidateCar.getCarSpecifications());

        return score;
    }

    /**
     * Helper class to associate a car with its similarity score.
     */
    private static class CarSimilarity {
        private Car car;
        private int similarityScore;

        public CarSimilarity(Car car, int similarityScore) {
            this.car = car;
            this.similarityScore = similarityScore;
        }

        public Car getCar() {
            return car;
        }

        public int getSimilarityScore() {
            return similarityScore;
        }
    }
}

