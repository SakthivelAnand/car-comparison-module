package com.car360.carcomparison.car_comparison_module.controller;

import com.car360.carcomparison.car_comparison_module.dto.CarDTO;
import com.car360.carcomparison.car_comparison_module.exception.ResourceNotFoundException;
import com.car360.carcomparison.car_comparison_module.model.Car;
import com.car360.carcomparison.car_comparison_module.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @Autowired
    private ObjectMapper objectMapper;

    private CarDTO carDTO;
    private Car car;

    @BeforeEach
    void setUp() {
        // Initialize CarDTO with valid data
        carDTO = new CarDTO();
        carDTO.setBrand("Toyota");
        carDTO.setName("Camry");
        carDTO.setModelYear(2020);
        carDTO.setCategory("Sedan");
        carDTO.setPriceRange(25000);

        // Initialize Car entity
        car = new Car();
        car.setCarId(1);
        car.setBrand("Toyota");
        car.setName("Camry");
        car.setModelYear(2020);
        car.setCategory("Sedan");
        car.setPriceRange(25000);
    }

    /**
     * Test the POST /api/cars endpoint for successful car creation.
     */
    @Test
    void testCreateCar_Success() throws Exception {
        // Mock the CarService to return the created Car
        given(carService.createCar(ArgumentMatchers.any(CarDTO.class))).willReturn(car);

        // Perform the POST request
        mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.car_id").value(car.getCarId()))
                .andExpect(jsonPath("$.brand").value(car.getBrand()))
                .andExpect(jsonPath("$.name").value(car.getName()))
                .andExpect(jsonPath("$.model_year").value(car.getModelYear()))
                .andExpect(jsonPath("$.category").value(car.getCategory()))
                .andExpect(jsonPath("$.price_range").value(car.getPriceRange()));
    }

    /**
     * Test the POST /api/cars endpoint with invalid input (e.g., missing brand).
     */
    @Test
    void testCreateCar_InvalidInput() throws Exception {
        // Create a CarDTO with missing brand
        CarDTO invalidCarDTO = new CarDTO();
        // invalidCarDTO.setBrand(null); // Omitted
        invalidCarDTO.setName("Camry");
        invalidCarDTO.setModelYear(2020);
        invalidCarDTO.setCategory("Sedan");
        invalidCarDTO.setPriceRange(25000);

        // Perform the POST request
        mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCarDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Assuming the validation error returns a specific structure
                .andExpect(jsonPath("$.errors", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.errors[*].field", hasItem("brand")))
                .andExpect(jsonPath("$.errors[*].message", hasItem("Brand is required.")));
    }

    /**
     * Test the GET /api/cars/{carId} endpoint for retrieving an existing car.
     */
    @Test
    void testGetCarById_Success() throws Exception {
        // Mock the CarService to return the Car
        given(carService.getCarById(1)).willReturn(car);

        // Perform the GET request
        mockMvc.perform(get("/api/cars/{carId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.car_id").value(car.getCarId()))
                .andExpect(jsonPath("$.brand").value(car.getBrand()))
                .andExpect(jsonPath("$.name").value(car.getName()))
                .andExpect(jsonPath("$.model_year").value(car.getModelYear()))
                .andExpect(jsonPath("$.category").value(car.getCategory()))
                .andExpect(jsonPath("$.price_range").value(car.getPriceRange()));
    }

    /**
     * Test the GET /api/cars/{carId} endpoint with a non-existing carId.
     */
    @Test
    void testGetCarById_NotFound() throws Exception {
        // Mock the CarService to throw an exception or return null
        given(carService.getCarById(999)).willThrow(new ResourceNotFoundException("Car not found with ID: 999"));
        // Alternatively, if the service throws an exception:
        // given(carService.getCarById(999)).willThrow(new ResourceNotFoundException("Car not found"));

        // Perform the GET request
        mockMvc.perform(get("/api/cars/{carId}", 999))
                .andExpect(status().isNotFound());
        // Additional assertions based on how the controller handles not found
    }

    /**
     * Test the PUT /api/cars/{carId} endpoint for successful car update.
     */
    @Test
    void testUpdateCar_Success() throws Exception {
        // Mock the CarService to return the updated Car
        Car updatedCar = new Car();
        updatedCar.setCarId(1);
        updatedCar.setBrand("Honda");
        updatedCar.setName("Accord");
        updatedCar.setModelYear(2021);
        updatedCar.setCategory("Sedan");
        updatedCar.setPriceRange(27000);

        given(carService.updateCar(eq(1), ArgumentMatchers.any(CarDTO.class))).willReturn(updatedCar);

        // Create a CarDTO with updated details
        CarDTO updatedCarDTO = new CarDTO();
        updatedCarDTO.setBrand("Honda");
        updatedCarDTO.setName("Accord");
        updatedCarDTO.setModelYear(2021);
        updatedCarDTO.setCategory("Sedan");
        updatedCarDTO.setPriceRange(27000);

        // Perform the PUT request
        mockMvc.perform(put("/api/cars/{carId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCarDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.car_id").value(updatedCar.getCarId()))
                .andExpect(jsonPath("$.brand").value(updatedCar.getBrand()))
                .andExpect(jsonPath("$.name").value(updatedCar.getName()))
                .andExpect(jsonPath("$.model_year").value(updatedCar.getModelYear()))
                .andExpect(jsonPath("$.category").value(updatedCar.getCategory()))
                .andExpect(jsonPath("$.price_range").value(updatedCar.getPriceRange()));
    }


    /**
     * Test the DELETE /api/cars/{carId} endpoint for successful car deletion.
     */
    @Test
    void testDeleteCar_Success() throws Exception {
        // Mock the CarService to perform deletion (void method)
        willDoNothing().given(carService).deleteCar(1);

        // Perform the DELETE request
        mockMvc.perform(delete("/api/cars/{carId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Car with ID 1 has been deleted successfully."));
    }

    /**
     * Test the DELETE /api/cars/{carId} endpoint with a non-existing carId.
     */
    @Test
    void testDeleteCar_NotFound() throws Exception {
        // Mock the CarService to throw an exception when car not found
        willThrow(new ResourceNotFoundException("Car not found with ID: 999")).given(carService).deleteCar(999);

        // Perform the DELETE request
        mockMvc.perform(delete("/api/cars/{carId}", 999))
                .andExpect(status().isNotFound());
    }

    /**
     * Test the GET /api/cars endpoint for retrieving all cars with default pagination.
     */
    @Test
    void testGetAllCars_DefaultPagination() throws Exception {
        // Prepare a Page of Cars
        Car car1 = new Car();
        car1.setCarId(1);
        car1.setBrand("Toyota");
        car1.setName("Camry");
        car1.setModelYear(2020);
        car1.setCategory("Sedan");
        car1.setPriceRange(25000);

        Car car2 = new Car();
        car2.setCarId(2);
        car2.setBrand("Honda");
        car2.setName("Accord");
        car2.setModelYear(2021);
        car2.setCategory("Sedan");
        car2.setPriceRange(27000);

        List<Car> carList = Arrays.asList(car1, car2);
        Page<Car> carPage = new PageImpl<>(carList, PageRequest.of(0, 10), carList.size());

        // Mock the CarService
        given(carService.getAllCars(any())).willReturn(carPage);

        // Perform the GET request with default pagination
        mockMvc.perform(get("/api/cars")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verify pagination fields
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].car_id").value(car1.getCarId()))
                .andExpect(jsonPath("$.content[1].car_id").value(car2.getCarId()))
                .andExpect(jsonPath("$.pageable.page_number").value(0))
                .andExpect(jsonPath("$.pageable.page_size").value(10))
                .andExpect(jsonPath("$.total_elements").value(2));
    }

    /**
     * Test the GET /api/cars endpoint for retrieving all cars with specific pagination parameters.
     */
    @Test
    void testGetAllCars_SpecificPagination() throws Exception {
        // Prepare a Page of Cars
        Car car1 = new Car();
        car1.setCarId(1);
        car1.setBrand("Toyota");
        car1.setName("Camry");
        car1.setModelYear(2020);
        car1.setCategory("Sedan");
        car1.setPriceRange(25000);

        List<Car> carList = Collections.singletonList(car1);
        Page<Car> carPage = new PageImpl<>(carList, PageRequest.of(1, 5), 6);

        // Mock the CarService
        given(carService.getAllCars(PageRequest.of(1, 5))).willReturn(carPage);

        // Perform the GET request with specific pagination
        mockMvc.perform(get("/api/cars")
                .param("page", "1")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verify pagination fields
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].car_id").value(car1.getCarId()))
                .andExpect(jsonPath("$.pageable.page_number").value(1))
                .andExpect(jsonPath("$.pageable.page_size").value(5))
                .andExpect(jsonPath("$.total_elements").value(6));
    }

    /**
     * Test the GET /api/cars/search endpoint with all search parameters.
     */
    @Test
    void testSearchCars_AllParameters() throws Exception {
        // Prepare a list of Cars matching the search criteria
        Car car1 = new Car();
        car1.setCarId(1);
        car1.setBrand("Toyota");
        car1.setName("Camry");
        car1.setModelYear(2020);
        car1.setCategory("Sedan");
        car1.setPriceRange(25000);

        List<Car> carList = Collections.singletonList(car1);

        // Mock the CarService
        given(carService.searchCars("Toyota", "Camry", 2020, "Sedan", 25000)).willReturn(carList);

        // Perform the GET request with all search parameters
        mockMvc.perform(get("/api/cars/search")
                .param("brand", "Toyota")
                .param("model", "Camry")
                .param("year", "2020")
                .param("category", "Sedan")
                .param("priceRange", "25000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].car_id").value(car1.getCarId()))
                .andExpect(jsonPath("$[0].brand").value(car1.getBrand()))
                .andExpect(jsonPath("$[0].name").value(car1.getName()))
                .andExpect(jsonPath("$[0].model_year").value(car1.getModelYear()))
                .andExpect(jsonPath("$[0].category").value(car1.getCategory()))
                .andExpect(jsonPath("$[0].price_range").value(car1.getPriceRange()));
    }

    /**
     * Test the GET /api/cars/search endpoint with partial search parameters.
     */
    @Test
    void testSearchCars_PartialParameters() throws Exception {
        // Prepare a list of Cars matching the partial search criteria
        Car car1 = new Car();
        car1.setCarId(1);
        car1.setBrand("Toyota");
        car1.setName("Camry");
        car1.setModelYear(2020);
        car1.setCategory("Sedan");
        car1.setPriceRange(25000);

        Car car2 = new Car();
        car2.setCarId(2);
        car2.setBrand("Toyota");
        car2.setName("Corolla");
        car2.setModelYear(2019);
        car2.setCategory("Sedan");
        car2.setPriceRange(20000);

        List<Car> carList = Arrays.asList(car1, car2);

        // Mock the CarService
        given(carService.searchCars("Toyota", null, null, "Sedan", null)).willReturn(carList);

        // Perform the GET request with partial search parameters
        mockMvc.perform(get("/api/cars/search")
                .param("brand", "Toyota")
                .param("category", "Sedan")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].brand").value("Toyota"))
                .andExpect(jsonPath("$[1].brand").value("Toyota"))
                .andExpect(jsonPath("$[0].category").value("Sedan"))
                .andExpect(jsonPath("$[1].category").value("Sedan"));
    }

    /**
     * Test the GET /api/cars/{carId}/suggestions endpoint for retrieving similar cars with default limit.
     */
    @Test
    void testGetSimilarCars_DefaultLimit() throws Exception {
        // Prepare a list of similar Cars
        Car similarCar1 = new Car();
        similarCar1.setCarId(3);
        similarCar1.setBrand("Toyota");
        similarCar1.setName("Corolla");
        similarCar1.setModelYear(2020);
        similarCar1.setCategory("Sedan");
        similarCar1.setPriceRange(22000);

        List<Car> similarCars = Collections.singletonList(similarCar1);

        // Mock the CarService
        given(carService.suggestSimilarCars(1, 10)).willReturn(similarCars);

        // Perform the GET request with default limit
        mockMvc.perform(get("/api/cars/{carId}/suggestions", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].car_id").value(similarCar1.getCarId()))
                .andExpect(jsonPath("$[0].brand").value(similarCar1.getBrand()))
                .andExpect(jsonPath("$[0].name").value(similarCar1.getName()));
    }

    /**
     * Test the GET /api/cars/{carId}/suggestions endpoint for retrieving similar cars with specific limit.
     */
    @Test
    void testGetSimilarCars_SpecificLimit() throws Exception {
        // Prepare a list of similar Cars
        Car similarCar1 = new Car();
        similarCar1.setCarId(3);
        similarCar1.setBrand("Toyota");
        similarCar1.setName("Corolla");
        similarCar1.setModelYear(2020);
        similarCar1.setCategory("Sedan");
        similarCar1.setPriceRange(22000);

        Car similarCar2 = new Car();
        similarCar2.setCarId(4);
        similarCar2.setBrand("Toyota");
        similarCar2.setName("Yaris");
        similarCar2.setModelYear(2019);
        similarCar2.setCategory("Hatchback");
        similarCar2.setPriceRange(18000);

        List<Car> similarCars = Arrays.asList(similarCar1, similarCar2);

        // Mock the CarService
        given(carService.suggestSimilarCars(1, 2)).willReturn(similarCars);

        // Perform the GET request with specific limit
        mockMvc.perform(get("/api/cars/{carId}/suggestions", 1)
                .param("limit", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].car_id").value(similarCar1.getCarId()))
                .andExpect(jsonPath("$[1].car_id").value(similarCar2.getCarId()));
    }

    /**
     * Test the GET /api/cars/{carId}/suggestions endpoint with a non-existing carId.
     */
    @Test
    void testGetSimilarCars_NotFound() throws Exception {
        // Mock the CarService to return an empty list or throw an exception
        given(carService.suggestSimilarCars(999, 10))
                .willThrow(new ResourceNotFoundException("Car not found"));

        // Perform the GET request
        mockMvc.perform(get("/api/cars/{carId}/suggestions", 999)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

