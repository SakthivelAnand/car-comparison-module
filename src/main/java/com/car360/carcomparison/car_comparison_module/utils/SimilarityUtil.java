package com.car360.carcomparison.car_comparison_module.utils;

import com.car360.carcomparison.car_comparison_module.model.Car;
import com.car360.carcomparison.car_comparison_module.repository.CarRepository;
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

    /**
     * Finds similar cars to the base car based on defined similarity criteria.
     *
     * @param baseCar The car to compare against.
     * @param limit   The maximum number of similar cars to return.
     * @return A list of similar cars.
     */
    public List<Car> findSimilarCars(Car baseCar, int limit) {
        // Extract attributes from the base car
        String brand = baseCar.getBrand();
        String category = baseCar.getCategory();
        Integer priceRange = baseCar.getPriceRange();
        Integer modelYear = baseCar.getModelYear();

        // Define similarity criteria thresholds
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

        // Assign similarity scores based on the number of matching criteria
        List<Car> similarCars = candidateCars.stream()
                .map(car -> new CarSimilarity(car, calculateSimilarityScore(baseCar, car)))
                .sorted(Comparator.comparingInt(CarSimilarity::getSimilarityScore).reversed()) // Sort by highest similarity
                .limit(limit) // Limit to the specified number
                .map(CarSimilarity::getCar)
                .collect(Collectors.toList());

        return similarCars;
    }

    /**
     * Calculates a similarity score between two cars based on matching criteria.
     *
     * @param baseCar    The base car.
     * @param compareCar The car to compare against.
     * @return An integer representing the similarity score.
     */
    private int calculateSimilarityScore(Car baseCar, Car compareCar) {
        int score = 0;

        // Check if the brand matches
        if (baseCar.getBrand().equalsIgnoreCase(compareCar.getBrand())) {
            score += 1;
        }

        // Check if the category matches
        if (baseCar.getCategory().equalsIgnoreCase(compareCar.getCategory())) {
            score += 1;
        }

        // Check if the price range is within tolerance
        if (Math.abs(baseCar.getPriceRange() - compareCar.getPriceRange()) <= 1) {
            score += 1;
        }

        // Check if the model year is within tolerance
        if (Math.abs(baseCar.getModelYear() - compareCar.getModelYear()) <= 1) {
            score += 1;
        }

        // Add more criteria and adjust scoring as needed

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

