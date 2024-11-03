package com.car360.carcomparison.car_comparison_module.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.car360.carcomparison.car_comparison_module.dto.CarDTO;
import com.car360.carcomparison.car_comparison_module.exception.ResourceNotFoundException;
import com.car360.carcomparison.car_comparison_module.model.Car;
import com.car360.carcomparison.car_comparison_module.repository.CarRepository;
import com.car360.carcomparison.car_comparison_module.repository.CarSpecificationRepository;
import com.car360.carcomparison.car_comparison_module.service.impl.CarServiceImpl;
import com.car360.carcomparison.car_comparison_module.utils.SimilarityUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceImplTest {

    @InjectMocks
    private CarServiceImpl carService;

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarSpecificationRepository carSpecRepository;

    @Mock
    private SimilarityUtil similarityUtil;

    @Test
    public void testCreateCar_Success() {
        CarDTO carDTO = new CarDTO();
        carDTO.setName("Test Car");
        carDTO.setBrand("Test Brand");
        carDTO.setModelYear(2021);
        carDTO.setCategory("SUV");
        carDTO.setPriceRange(30000);
        carDTO.setImageUrl("http://example.com/image.jpg");
        carDTO.setDescription("Test Description");

        Car savedCar = new Car();
        savedCar.setCarId(1);
        savedCar.setName(carDTO.getName());
        savedCar.setBrand(carDTO.getBrand());
        savedCar.setModelYear(carDTO.getModelYear());
        savedCar.setCategory(carDTO.getCategory());
        savedCar.setPriceRange(carDTO.getPriceRange());
        savedCar.setImageUrl(carDTO.getImageUrl());
        savedCar.setDescription(carDTO.getDescription());

        when(carRepository.save(any(Car.class))).thenReturn(savedCar);

        Car result = carService.createCar(carDTO);

        assertNotNull(result);
        assertEquals(savedCar.getCarId(), result.getCarId());
        assertEquals(savedCar.getName(), result.getName());
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    public void testGetCarById_Success() {
        Integer carId = 1;
        Car car = new Car();
        car.setCarId(carId);
        car.setName("Test Car");
        car.setBrand("Test Brand");
        car.setCarSpecifications(new ArrayList<>());

        when(carRepository.findWithSpecificationsByCarId(carId)).thenReturn(Optional.of(car));

        Car result = carService.getCarById(carId);

        assertNotNull(result);
        assertEquals(carId, result.getCarId());
        verify(carRepository, times(1)).findWithSpecificationsByCarId(carId);
    }

    @Test
    public void testGetCarById_NotFound() {
        Integer carId = 1;

        when(carRepository.findWithSpecificationsByCarId(carId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> carService.getCarById(carId));
        verify(carRepository, times(1)).findWithSpecificationsByCarId(carId);
    }

    @Test
    public void testGetCarsByIds_Success() {
        List<Integer> carIds = Arrays.asList(1, 2, 3);
        Car car1 = new Car();
        car1.setCarId(1);
        Car car2 = new Car();
        car2.setCarId(2);
        Car car3 = new Car();
        car3.setCarId(3);

        when(carRepository.findAllById(carIds)).thenReturn(Arrays.asList(car1, car2, car3));

        List<Car> result = carService.getCarsByIds(carIds);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(carRepository, times(1)).findAllById(carIds);
    }

    @Test
    public void testGetCarsByIds_MissingIds() {
        List<Integer> carIds = Arrays.asList(1, 2, 3);
        Car car1 = new Car();
        car1.setCarId(1);
        Car car2 = new Car();
        car2.setCarId(2);

        when(carRepository.findAllById(carIds)).thenReturn(Arrays.asList(car1, car2));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> carService.getCarsByIds(carIds));
        assertTrue(exception.getMessage().contains("Cars not found with IDs: [3]"));
        verify(carRepository, times(1)).findAllById(carIds);
    }

    @Test
    public void testUpdateCar_Success() {
        Integer carId = 1;
        Car existingCar = new Car();
        existingCar.setCarId(carId);
        existingCar.setName("Old Name");
        existingCar.setCarSpecifications(new ArrayList<>());

        CarDTO carDTO = new CarDTO();
        carDTO.setName("New Name");
        carDTO.setBrand("New Brand");
        carDTO.setModelYear(2022);
        carDTO.setCategory("Sedan");
        carDTO.setPriceRange(35000);
        carDTO.setImageUrl("http://example.com/new_image.jpg");
        carDTO.setDescription("Updated Description");

        when(carRepository.findWithSpecificationsByCarId(carId)).thenReturn(Optional.of(existingCar));
        when(carRepository.save(existingCar)).thenReturn(existingCar);

        Car result = carService.updateCar(carId, carDTO);

        assertNotNull(result);
        assertEquals(carDTO.getName(), result.getName());
        assertEquals(carDTO.getBrand(), result.getBrand());
        verify(carRepository, times(1)).save(existingCar);
    }

    @Test
    public void testUpdateCar_NotFound() {
        Integer carId = 1;
        CarDTO carDTO = new CarDTO();

        when(carRepository.findWithSpecificationsByCarId(carId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> carService.updateCar(carId, carDTO));
        verify(carRepository, never()).save(any());
    }

    @Test
    public void testDeleteCar_Success() {
        Integer carId = 1;
        Car existingCar = new Car();
        existingCar.setCarId(carId);
        existingCar.setCarSpecifications(new ArrayList<>());

        when(carRepository.findWithSpecificationsByCarId(carId)).thenReturn(Optional.of(existingCar));

        carService.deleteCar(carId);

        verify(carRepository, times(1)).delete(existingCar);
    }

    @Test
    public void testDeleteCar_NotFound() {
        Integer carId = 1;

        when(carRepository.findWithSpecificationsByCarId(carId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> carService.deleteCar(carId));
        verify(carRepository, never()).delete(any());
    }

    @Test
    public void testGetAllCars_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Car> cars = Arrays.asList(new Car(), new Car());
        Page<Car> carPage = new PageImpl<>(cars, pageable, cars.size());

        when(carRepository.findAll(pageable)).thenReturn(carPage);

        Page<Car> result = carService.getAllCars(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(carRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testSearchCars_ByBrand() {
        String brand = "Test Brand";
        List<Car> cars = Arrays.asList(new Car(), new Car());

        when(carRepository.findByBrandContainingIgnoreCase(brand)).thenReturn(cars);

        List<Car> result = carService.searchCars(brand, null, null, null, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(carRepository, times(1)).findByBrandContainingIgnoreCase(brand);
    }

    @Test
    public void testSearchCars_ByCategory() {
        String category = "SUV";
        List<Car> cars = Arrays.asList(new Car(), new Car());

        when(carRepository.findByCategoryContainingIgnoreCase(category)).thenReturn(cars);

        List<Car> result = carService.searchCars(null, null, null, category, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(carRepository, times(1)).findByCategoryContainingIgnoreCase(category);
    }

    @Test
    public void testSearchCars_ByYear() {
        Integer year = 2021;
        List<Car> cars = Arrays.asList(new Car(), new Car());

        when(carRepository.findByModelYear(year)).thenReturn(cars);

        List<Car> result = carService.searchCars(null, null, year, null, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(carRepository, times(1)).findByModelYear(year);
    }

    @Test
    public void testSuggestSimilarCars_Success() {
        Integer carId = 1;
        int limit = 5;
        Car baseCar = new Car();
        baseCar.setCarId(carId);
        baseCar.setCarSpecifications(new ArrayList<>());

        List<Car> similarCars = Arrays.asList(new Car(), new Car());

        when(carRepository.findWithSpecificationsByCarId(carId)).thenReturn(Optional.of(baseCar));
        when(similarityUtil.findSimilarCars(baseCar, limit)).thenReturn(similarCars);

        List<Car> result = carService.suggestSimilarCars(carId, limit);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(similarityUtil, times(1)).findSimilarCars(baseCar, limit);
    }

    @Test
    public void testSuggestSimilarCars_NotFound() {
        Integer carId = 1;
        int limit = 5;

        when(carRepository.findWithSpecificationsByCarId(carId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> carService.suggestSimilarCars(carId, limit));
        verify(similarityUtil, never()).findSimilarCars(any(), anyInt());
    }
}

