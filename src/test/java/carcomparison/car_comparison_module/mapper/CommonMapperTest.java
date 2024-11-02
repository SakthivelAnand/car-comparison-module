package carcomparison.car_comparison_module.mapper;

import com.car360.carcomparison.car_comparison_module.dto.CarResponseDTO;
import com.car360.carcomparison.car_comparison_module.dto.CarSpecificationDTO;
import com.car360.carcomparison.car_comparison_module.dto.ComparisonHistoryDTO;
import com.car360.carcomparison.car_comparison_module.mapper.CommonMapper;
import com.car360.carcomparison.car_comparison_module.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonMapperTest {

    private CommonMapper commonMapper;

    @BeforeEach
    public void setUp() {
        commonMapper = new CommonMapper();
    }

    @Test
    public void testToComparisonHistoryDTO_NullInput() {
        // Act
        ComparisonHistoryDTO result = commonMapper.toComparisonHistoryDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    public void testToComparisonHistoryDTO_ValidInput() {
        Comparison comparison = createComparison();
        ComparisonHistoryDTO result = commonMapper.toComparisonHistoryDTO(comparison);

        // Assert
        assertNotNull(result);
        assertEquals(comparison.getId(), result.getComparisonId());
        assertEquals(comparison.getTimestamp(), result.getTimestamp());
        assertEquals(2, result.getComparedCars().size());

        System.out.println("Failing Case : " + result.toString());

        assertEquals(2, result.getComparedCars().size());
    }

    @Test
    public void testToCarResponseDTO_NullInput() {
        // Act
        CarResponseDTO result = commonMapper.toCarResponseDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    public void testToCarResponseDTO_ValidInput() {
        // Arrange
        Car car = createCar(1, "Test Car");

        // Act
        CarResponseDTO result = commonMapper.toCarResponseDTO(car);

        // Assert
        assertNotNull(result);
        assertEquals(car.getCarId(), result.getCarId());
        assertEquals(car.getName(), result.getName());
        assertEquals(car.getBrand(), result.getBrand());
        assertEquals(car.getModelYear(), result.getModelYear());
        assertEquals(car.getCategory(), result.getCategory());
        assertEquals(car.getPriceRange(), result.getPriceRange());
        assertEquals(car.getImageUrl(), result.getImageUrl());
        assertEquals(car.getDescription(), result.getDescription());
        assertEquals(2, result.getCarSpecifications().size());

        // Verify specifications
        CarSpecificationDTO specDTO = result.getCarSpecifications().get(0);
        assertEquals("Engine", specDTO.getSpecificationName());
        assertEquals("V6", specDTO.getValue());

        specDTO = result.getCarSpecifications().get(1);
        assertEquals("Color", specDTO.getSpecificationName());
        assertEquals("Red", specDTO.getValue());
    }

    @Test
    public void testToCarSpecificationDTO_NullInput() {
        // Act
        CarSpecificationDTO result = commonMapper.toCarSpecificationDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    public void testToCarSpecificationDTO_NullSpecification() {
        // Arrange
        CarSpecification carSpec = new CarSpecification();
        carSpec.setValue("V6");

        // Act
        CarSpecificationDTO result = commonMapper.toCarSpecificationDTO(carSpec);

        // Assert
        assertNull(result);
    }

    @Test
    public void testToCarSpecificationDTO_ValidInput() {
        // Arrange
        Specification specification = new Specification();
        specification.setName("Engine");

        CarSpecification carSpec = new CarSpecification();
        carSpec.setSpecification(specification);
        carSpec.setValue("V6");

        // Act
        CarSpecificationDTO result = commonMapper.toCarSpecificationDTO(carSpec);

        // Assert
        assertNotNull(result);
        assertEquals("Engine", result.getSpecificationName());
        assertEquals("V6", result.getValue());
    }

    @Test
    public void testToComparisonHistoryDTOs_EmptyList() {
        // Arrange
        List<Comparison> comparisons = new ArrayList<>();

        // Act
        List<ComparisonHistoryDTO> result = commonMapper.toComparisonHistoryDTOs(comparisons);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testToComparisonHistoryDTOs_ValidInput() {
        // Arrange
        Comparison comparison1 = createComparison();
        Comparison comparison2 = createComparison();
        comparison2.setId(2L);
        comparison2.setTimestamp(LocalDateTime.now().minusDays(1));

        List<Comparison> comparisons = Arrays.asList(comparison1, comparison2);

        // Act
        List<ComparisonHistoryDTO> result = commonMapper.toComparisonHistoryDTOs(comparisons);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify first comparison
        ComparisonHistoryDTO dto1 = result.get(0);
        assertEquals(comparison1.getId(), dto1.getComparisonId());
        assertEquals(comparison1.getTimestamp(), dto1.getTimestamp());
        assertEquals(2, dto1.getComparedCars().size());

        // Verify second comparison
        ComparisonHistoryDTO dto2 = result.get(1);
        assertEquals(comparison2.getId(), dto2.getComparisonId());
        assertEquals(comparison2.getTimestamp(), dto2.getTimestamp());
        assertEquals(2, dto2.getComparedCars().size());
    }

    // Helper methods to create test data

    private Comparison createComparison() {
        Comparison comparison = new Comparison();
        comparison.setId(1L);
        comparison.setTimestamp(LocalDateTime.now());

        Car car1 = createCar(1, "Car 1");
        Car car2 = createCar(2, "Car 2");

        ComparisonItem item1 = new ComparisonItem();
        item1.setCar(car1);
        item1.setComparison(comparison);

        ComparisonItem item2 = new ComparisonItem();
        item2.setCar(car2);
        item2.setComparison(comparison);

        Set<ComparisonItem> items = new HashSet<>();
        items.add(item1);
        items.add(item2);

        comparison.setComparisonItems(items);

        return comparison;
    }

    private Car createCar(Integer carId, String name) {
        Car car = new Car();
        car.setCarId(carId);
        car.setName(name);
        car.setBrand("Brand A");
        car.setModelYear(2020);
        car.setCategory("Sedan");
        car.setPriceRange(30000);
        car.setImageUrl("http://example.com/car.jpg");
        car.setDescription("Description of the car.");

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

        List<CarSpecification> carSpecifications = Arrays.asList(carSpec1, carSpec2);
        car.setCarSpecifications(carSpecifications);

        return car;
    }
}

