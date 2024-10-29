package com.car360.carcomparison.car_comparison_module.service;


import com.car360.carcomparison.car_comparison_module.dto.CarDTO;
import com.car360.carcomparison.car_comparison_module.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarService {

    // Create a new car
    Car createCar(CarDTO carDTO);

    // Get a car by ID
    Car getCarById(Integer carId);

    List<Car> getCarsByIds(List<Integer> carIds);

    // Update a car
    Car updateCar(Integer carId, CarDTO carDTO);

    // Delete a car
    void deleteCar(Integer carId);

    // List all cars with pagination
    Page<Car> getAllCars(Pageable pageable);

    // Search cars based on criteria
    List<Car> searchCars(String brand, String model, Integer year, String category, Integer priceRange);

    List<Car> suggestSimilarCars(Integer carId, int limit);
}

