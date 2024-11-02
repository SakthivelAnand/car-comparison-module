package com.car360.carcomparison.car_comparison_module.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.car360.carcomparison.car_comparison_module.dto.*;
import com.car360.carcomparison.car_comparison_module.exception.ResourceNotFoundException;
import com.car360.carcomparison.car_comparison_module.mapper.CommonMapper;
import com.car360.carcomparison.car_comparison_module.model.*;
import com.car360.carcomparison.car_comparison_module.repository.CarRepository;
import com.car360.carcomparison.car_comparison_module.repository.ComparisonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class CarComparisonServiceImplTest {

    @InjectMocks
    private CarComparisonServiceImpl carComparisonService;

    @Mock
    private CarService carService;

    @Mock
    private ComparisonRepository comparisonRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private CommonMapper commonMapper;

    private Car baseCar;
    private Car compareCar1;
    private Car compareCar2;
    private User user;

    @BeforeEach
    public void setUp() {
        // Initialize test data
        baseCar = createCar(1, "Base Car");
        compareCar1 = createCar(2, "Compare Car 1");
        compareCar2 = createCar(3, "Compare Car 2");

        user = new User();
        user.setUserId(1L);
        user.setUsername("Test User");
    }

    private Car createCar(Integer carId, String name) {
        Car car = new Car();
        car.setCarId(carId);
        car.setName(name);

        Specification spec1 = new Specification();
        spec1.setName("Engine");
        Specification spec2 = new Specification();
        spec2.setName("Color");

        CarSpecification carSpec1 = new CarSpecification();
        carSpec1.setSpecification(spec1);
        carSpec1.setValue("V6");

        CarSpecification carSpec2 = new CarSpecification();
        carSpec2.setSpecification(spec2);
        carSpec2.setValue("Red");

        car.setCarSpecifications(Arrays.asList(carSpec1, carSpec2));

        return car;
    }

    @Test
    public void testCompareCars_Success() {
        // Arrange
        CompareRequestDTO compareRequest = new CompareRequestDTO();
        compareRequest.setBaseCarId(baseCar.getCarId());
        compareRequest.setCompareCarIds(Arrays.asList(compareCar1.getCarId(), compareCar2.getCarId()));
        compareRequest.setShowOnlyDifferences(false);

        when(carService.getCarById(baseCar.getCarId())).thenReturn(baseCar);
        when(carService.getCarsByIds(anyList())).thenReturn(Arrays.asList(compareCar1, compareCar2));

        CompareResponseDTO response = carComparisonService.compareCars(compareRequest);

        assertNotNull(response);
        assertEquals(baseCar.getCarId(), response.getBaseCar().getCarId());
        assertEquals(2, response.getComparisons().size());

        verify(carService, times(1)).getCarById(baseCar.getCarId());
        verify(carService, times(1)).getCarsByIds(anyList());
    }

    @Test
    public void testCompareCars_ShowOnlyDifferences() {
        compareCar1.getCarSpecifications().get(0).setValue("V8"); // Different engine
        compareCar1.getCarSpecifications().get(1).setValue("Red"); // Same color

        CompareRequestDTO compareRequest = new CompareRequestDTO();
        compareRequest.setBaseCarId(baseCar.getCarId());
        compareRequest.setCompareCarIds(Collections.singletonList(compareCar1.getCarId()));
        compareRequest.setShowOnlyDifferences(true);

        when(carService.getCarById(baseCar.getCarId())).thenReturn(baseCar);
        when(carService.getCarsByIds(anyList())).thenReturn(Collections.singletonList(compareCar1));

        // Act
        CompareResponseDTO response = carComparisonService.compareCars(compareRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getComparisons().size());
        ComparisonDTO comparisonDTO = response.getComparisons().get(0);
        assertNotNull(comparisonDTO.getDifferencesOnly());
        assertEquals(1, comparisonDTO.getDifferencesOnly().size());
        assertEquals("V8", comparisonDTO.getDifferencesOnly().get("Engine"));

        verify(carService, times(1)).getCarById(baseCar.getCarId());
        verify(carService, times(1)).getCarsByIds(anyList());
    }

    @Test
    public void testCompareCars_BaseCarNotFound() {
        // Arrange
        CompareRequestDTO compareRequest = new CompareRequestDTO();
        compareRequest.setBaseCarId(999); // Non-existent car ID
        compareRequest.setCompareCarIds(Arrays.asList(compareCar1.getCarId()));

        when(carService.getCarById(999)).thenThrow(new ResourceNotFoundException("Car not found"));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            carComparisonService.compareCars(compareRequest);
        });

        verify(carService, times(1)).getCarById(999);
        verify(carService, never()).getCarsByIds(anyList());
    }

    @Test
    public void testCompareCars_CompareCarsNotFound() {
        // Arrange
        CompareRequestDTO compareRequest = new CompareRequestDTO();
        compareRequest.setBaseCarId(baseCar.getCarId());
        compareRequest.setCompareCarIds(Arrays.asList(999)); // Non-existent car ID

        when(carService.getCarById(baseCar.getCarId())).thenReturn(baseCar);
        when(carService.getCarsByIds(anyList())).thenThrow(new ResourceNotFoundException("Cars not found"));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            carComparisonService.compareCars(compareRequest);
        });

        verify(carService, times(1)).getCarById(baseCar.getCarId());
        verify(carService, times(1)).getCarsByIds(anyList());
    }

    @Test
    public void testSaveComparison_Success() {
        // Arrange
        List<Integer> carIds = Arrays.asList(baseCar.getCarId(), compareCar1.getCarId());

        when(carRepository.findAllById(carIds)).thenReturn(Arrays.asList(baseCar, compareCar1));
        when(comparisonRepository.save(any(Comparison.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Comparison comparison = carComparisonService.saveComparison(user, carIds);

        // Assert
        assertNotNull(comparison);
        assertEquals(user, comparison.getUser());
        assertEquals(2, comparison.getComparisonItems().size());

        verify(carRepository, times(1)).findAllById(carIds);
        verify(comparisonRepository, times(1)).save(any(Comparison.class));
    }

    @Test
    public void testSaveComparison_CarsNotFound() {
        // Arrange
        List<Integer> carIds = Arrays.asList(999); // Non-existent car ID

        when(carRepository.findAllById(carIds)).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            carComparisonService.saveComparison(user, carIds);
        });

        verify(carRepository, times(1)).findAllById(carIds);
        verify(comparisonRepository, never()).save(any(Comparison.class));
    }

    @Test
    public void testGetComparisonHistory_Success() {
        // Arrange
        Comparison comparison1 = new Comparison();
        comparison1.setId(1L);
        comparison1.setUser(user);
        comparison1.setTimestamp(LocalDateTime.now());

        Comparison comparison2 = new Comparison();
        comparison2.setId(2L);
        comparison2.setUser(user);
        comparison2.setTimestamp(LocalDateTime.now().minusDays(1));

        List<Comparison> comparisons = Arrays.asList(comparison1, comparison2);

        when(comparisonRepository.findByUserOrderByTimestampDesc(user)).thenReturn(comparisons);


        // Create CarResponseDTO instances for the compared cars
        CarResponseDTO carResponse1 = new CarResponseDTO(1, "Car 1", "Brand A", 2020, "Sedan", 30000, "url1", "desc1", new ArrayList<>());
        CarResponseDTO carResponse2 = new CarResponseDTO(2, "Car 2", "Brand B", 2021, "SUV", 35000, "url2", "desc2", new ArrayList<>());
        CarResponseDTO carResponse3 = new CarResponseDTO(3, "Car 3", "Brand C", 2019, "Coupe", 25000, "url3", "desc3", new ArrayList<>());
        CarResponseDTO carResponse4 = new CarResponseDTO(4, "Car 4", "Brand D", 2018, "Hatchback", 20000, "url4", "desc4", new ArrayList<>());


        List<ComparisonHistoryDTO> historyDTOs = Arrays.asList(
                new ComparisonHistoryDTO(1L, LocalDateTime.now(), Arrays.asList(carResponse1, carResponse2)),
                new ComparisonHistoryDTO(2L, LocalDateTime.now().minusDays(1), Arrays.asList(carResponse3, carResponse4))
        );

        when(commonMapper.toComparisonHistoryDTOs(comparisons)).thenReturn(historyDTOs);

        // Act
        List<ComparisonHistoryDTO> result = carComparisonService.getComparisonHistory(user);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(comparisonRepository, times(1)).findByUserOrderByTimestampDesc(user);
        verify(commonMapper, times(1)).toComparisonHistoryDTOs(comparisons);
    }

    @Test
    public void testGetComparisonHistory_NoHistory() {
        // Arrange
        when(comparisonRepository.findByUserOrderByTimestampDesc(user)).thenReturn(Collections.emptyList());
        when(commonMapper.toComparisonHistoryDTOs(anyList())).thenReturn(Collections.emptyList());

        // Act
        List<ComparisonHistoryDTO> result = carComparisonService.getComparisonHistory(user);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(comparisonRepository, times(1)).findByUserOrderByTimestampDesc(user);
        verify(commonMapper, times(1)).toComparisonHistoryDTOs(anyList());
    }
}

