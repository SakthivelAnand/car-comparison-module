package com.car360.carcomparison.car_comparison_module.controller;


import com.car360.carcomparison.car_comparison_module.dto.CarDTO;
import com.car360.carcomparison.car_comparison_module.model.Car;
import com.car360.carcomparison.car_comparison_module.service.CarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    @Autowired
    private CarService carService;

    /**
     * Endpoint to create a new car.
     * Accessible by ADMIN users.
     *
     * @param carDTO Data Transfer Object containing car details.
     * @return The created Car entity.
     */
    @PostMapping
    public Car createCar(@Valid @RequestBody CarDTO carDTO) {
        return carService.createCar(carDTO);
    }

    /**
     * Endpoint to get details of a specific car by ID.
     *
     * @param carId ID of the car.
     * @return The Car entity.
     */
    @GetMapping("/{carId}")
    public Car getCarById(@PathVariable Integer carId) {
        return carService.getCarById(carId);
    }

    /**
     * Endpoint to update an existing car by ID.
     * Accessible by ADMIN users.
     *
     * @param carId  ID of the car to update.
     * @param carDTO Data Transfer Object containing updated car details.
     * @return The updated Car entity.
     */
    @PutMapping("/{carId}")
    public Car updateCar(@PathVariable Integer carId, @Valid @RequestBody CarDTO carDTO) {
        return carService.updateCar(carId, carDTO);
    }

    /**
     * Endpoint to delete a car by ID.
     * Accessible by ADMIN users.
     *
     * @param carId ID of the car to delete.
     * @return A success message or appropriate response.
     */
    @DeleteMapping("/{carId}")
    public String deleteCar(@PathVariable Integer carId) {
        carService.deleteCar(carId);
        return "Car with ID " + carId + " has been deleted successfully.";
    }

    /**
     * Endpoint to list all cars with pagination.
     *
     * @param page Page number (zero-based).
     * @param size Number of records per page.
     * @return A page of Car entities.
     */
    @GetMapping
    public Page<Car> getAllCars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return carService.getAllCars(pageable);
    }

    /**
     * Endpoint to search for cars based on various criteria.
     *
     * @param brand      Brand of the car.
     * @param model      Model name of the car.
     * @param year       Model year of the car.
     * @param category   Category of the car (e.g., SUV, Sedan).
     * @param priceRange Price range of the car.
     * @return A list of cars matching the search criteria.
     */
    @GetMapping("/search")
    public List<Car> searchCars(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer priceRange) {
        return carService.searchCars(brand, model, year, category, priceRange);
    }

    /**
     * GET /api/cars/{carId}/suggestions
     *
     * @param carId The ID of the car for which to find similar cars.
     * @return ResponseEntity containing a list of CarResponseDTOs.
     */
    @GetMapping("/{carId}/suggestions")
    public ResponseEntity<List<Car>> getSimilarCars(
            @PathVariable Integer carId,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<Car> similarCars = carService.suggestSimilarCars(carId, limit);
        return ResponseEntity.ok(similarCars);
    }
}
