package com.car360.carcomparison.car_comparison_module.service;


import com.car360.carcomparison.car_comparison_module.dto.CarDTO;
import com.car360.carcomparison.car_comparison_module.exception.ResourceNotFoundException;
import com.car360.carcomparison.car_comparison_module.model.Car;
import com.car360.carcomparison.car_comparison_module.model.CarSpecification;
import com.car360.carcomparison.car_comparison_module.repository.CarRepository;
import com.car360.carcomparison.car_comparison_module.repository.CarSpecificationRepository;
import com.car360.carcomparison.car_comparison_module.utils.SimilarityUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarSpecificationRepository carSpecRepository;

    @Autowired
    private SimilarityUtil similarityUtil;

    @Override
    public Car createCar(CarDTO carDTO) {
        Car car = new Car();
        car.setName(carDTO.getName());
        car.setBrand(carDTO.getBrand());
        car.setModelYear(carDTO.getModelYear());
        car.setCategory(carDTO.getCategory());
        car.setPriceRange(carDTO.getPriceRange());
        car.setImageUrl(carDTO.getImageUrl());
        car.setDescription(carDTO.getDescription());

        return carRepository.save(car);
    }

    @Override
    @Transactional(readOnly = true)
    public Car getCarById(Integer carId) {
        Car car = carRepository.findWithSpecificationsByCarId(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with ID: " + carId));
        car.getCarSpecifications().forEach(cs -> {
            System.out.println("Specification: " + cs.getSpecification().getName() + ", Value: " + cs.getValue());
        });
        return car;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Car> getCarsByIds(List<Integer> carIds) {
        List<Car> cars = carRepository.findAllById(carIds);
        if (cars.size() != carIds.size()) {
            List<Integer> foundIds = cars.stream().map(Car::getCarId).collect(Collectors.toList());
            List<Integer> missingIds = carIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toList());
            throw new ResourceNotFoundException("Cars not found with IDs: " + missingIds);
        }
        return cars;
    }

    @Override
    @Transactional
    public Car updateCar(Integer carId, CarDTO carDTO) {
        Car existingCar = getCarById(carId);

        existingCar.setName(carDTO.getName());
        existingCar.setBrand(carDTO.getBrand());
        existingCar.setModelYear(carDTO.getModelYear());
        existingCar.setCategory(carDTO.getCategory());
        existingCar.setPriceRange(carDTO.getPriceRange());
        existingCar.setImageUrl(carDTO.getImageUrl());
        existingCar.setDescription(carDTO.getDescription());

        return carRepository.save(existingCar);
    }

    @Override
    @Transactional
    public void deleteCar(Integer carId) {
        Car existingCar = getCarById(carId);
        carRepository.delete(existingCar);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Car> getAllCars(Pageable pageable) {
        return carRepository.findAll(pageable);
    }

    @Override
    public List<Car> searchCars(String brand, String model, Integer year, String category, Integer priceRange) {
        // Implement search logic based on provided criteria
        // This is a simple implementation; consider using Specifications or QueryDSL for more flexibility

        if (brand != null && !brand.isEmpty()) {
            return carRepository.findByBrandContainingIgnoreCase(brand);
        } else if (category != null && !category.isEmpty()) {
            return carRepository.findByCategoryContainingIgnoreCase(category);
        } else if (priceRange != null) {
            return carRepository.findByPriceRange(priceRange);
        } else if (year != null) {
            return carRepository.findByModelYear(year);
        } else {
            return carRepository.findAll();
        }
    }

    @Override
    public List<Car> suggestSimilarCars(Integer carId, int limit) {
        Car baseCar = getCarById(carId);
        return similarityUtil.findSimilarCars(baseCar, limit);
    }
}
