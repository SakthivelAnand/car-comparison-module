package com.car360.carcomparison.car_comparison_module.utils;

import com.car360.carcomparison.car_comparison_module.model.*;
import com.car360.carcomparison.car_comparison_module.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimilarityUtilTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private SimilarityUtil similarityUtil;

    private Car baseCar;
    private Car similarCar1;
    private Car similarCar2;
    private Car differentCar;

    @BeforeEach
    void setUp() {
        // Create base car for comparison
        baseCar = new Car();
        baseCar.setCarId(1);
        baseCar.setBrand("Toyota");
        baseCar.setCategory("SUV");
        baseCar.setPriceRange(3);
        baseCar.setModelYear(2020);
        baseCar.setCarSpecifications(createCarSpecifications(baseCar, "V6", "300"));

        // Create a car that should be considered similar based on attributes and specifications
        similarCar1 = new Car();
        similarCar1.setCarId(2);
        similarCar1.setBrand("Toyota");
        similarCar1.setCategory("SUV");
        similarCar1.setPriceRange(3);
        similarCar1.setModelYear(2020);
        similarCar1.setCarSpecifications(createCarSpecifications(similarCar1, "V6", "300"));

        // Create another car that is somewhat similar but with a different model year
        similarCar2 = new Car();
        similarCar2.setCarId(3);
        similarCar2.setBrand("Toyota");
        similarCar2.setCategory("SUV");
        similarCar2.setPriceRange(3);
        similarCar2.setModelYear(2019);
        similarCar2.setCarSpecifications(createCarSpecifications(similarCar2, "V6", "290"));

        // Create a car that should not be considered similar
        differentCar = new Car();
        differentCar.setCarId(4);
        differentCar.setBrand("Honda");
        differentCar.setCategory("Sedan");
        differentCar.setPriceRange(2);
        differentCar.setModelYear(2018);
        differentCar.setCarSpecifications(createCarSpecifications(differentCar, "I4", "150"));
    }

    private List<CarSpecification> createCarSpecifications(Car car, String engineType, String horsepower) {
        // Creating specifications
        Specification engineSpec = new Specification(1, "Engine Type", DataType.STRING);
        Specification hpSpec = new Specification(2, "Horsepower", DataType.FLOAT);

        // Creating CarSpecifications
        CarSpecification engineTypeSpec = new CarSpecification(new CarSpecificationId(car.getCarId(), engineSpec.getSpecId()), car, engineSpec, engineType);
        CarSpecification horsepowerSpec = new CarSpecification(new CarSpecificationId(car.getCarId(), hpSpec.getSpecId()), car, hpSpec, horsepower);

        return Arrays.asList(engineTypeSpec, horsepowerSpec);
    }

    @Test
    void testFindSimilarCars_withExactMatchCriteria() {
        // Mock repository to return a list of cars
        when(carRepository.findAll()).thenReturn(Arrays.asList(similarCar1, similarCar2, differentCar));

        // Execute the method to find similar cars
        List<Car> result = similarityUtil.findSimilarCars(baseCar, 2);

        // Verify the results
        assertEquals(2, result.size());
        assertEquals(similarCar1.getCarId(), result.get(0).getCarId());
        assertEquals(similarCar2.getCarId(), result.get(1).getCarId());
    }

    @Test
    void testFindSimilarCars_whenInsufficientExactMatches() {
        // Mock repository to return only one car that exactly matches, and other unrelated cars
        when(carRepository.findAll()).thenReturn(Arrays.asList(similarCar1, differentCar));

        // Execute the method with a limit of 2, expecting relaxed criteria
        List<Car> result = similarityUtil.findSimilarCars(baseCar, 2);

        // Verify the results
        assertEquals(1, result.size());
        assertEquals(similarCar1.getCarId(), result.get(0).getCarId());
    }

    @Test
    void testFindSimilarCars_withNoSimilarCars() {
        // Mock repository to return unrelated cars
        when(carRepository.findAll()).thenReturn(Collections.singletonList(differentCar));

        // Execute the method with a limit of 2
        List<Car> result = similarityUtil.findSimilarCars(baseCar, 2);

        // Verify the results - expecting no similar cars
        assertEquals(0, result.size());
    }

    @Test
    void testFindSimilarCars_withSpecificationScore() {
        // Modify similarCar2 to have a different specification, reducing its similarity
        similarCar2.setCarSpecifications(createCarSpecifications(similarCar2, "V8", "290")); // Different engine type

        // Mock repository to return cars including similarCar1 and modified similarCar2
        when(carRepository.findAll()).thenReturn(Arrays.asList(similarCar1, similarCar2, differentCar));

        // Execute the method with a limit of 2
        List<Car> result = similarityUtil.findSimilarCars(baseCar, 2);

        // Verify that similarCar1 has a higher similarity score than modified similarCar2
        assertEquals(2, result.size());
        assertEquals(similarCar1.getCarId(), result.get(0).getCarId());
        assertEquals(similarCar2.getCarId(), result.get(1).getCarId());
    }
}
