package com.car360.carcomparison.car_comparison_module.controller;

import com.car360.carcomparison.car_comparison_module.dto.CarSpecificationResponseDTO;
import com.car360.carcomparison.car_comparison_module.exception.ResourceNotFoundException;
import com.car360.carcomparison.car_comparison_module.service.CarSpecificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarSpecificationController.class)
public class CarSpecificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarSpecificationService carSpecificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateCarSpecification() throws Exception {
        CarSpecificationResponseDTO inputDto = new CarSpecificationResponseDTO();
        inputDto.setCarId(1);
        inputDto.setSpecId(2);
        inputDto.setName("Engine Type");
        inputDto.setValue("V8");

        CarSpecificationResponseDTO outputDto = new CarSpecificationResponseDTO(1, 2, "Engine Type", "V8");

        when(carSpecificationService.createCarSpecification(any(CarSpecificationResponseDTO.class))).thenReturn(outputDto);

        mockMvc.perform(post("/api/car-specifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.car_id").value(1))
                .andExpect(jsonPath("$.spec_id").value(2))
                .andExpect(jsonPath("$.name").value("Engine Type"))
                .andExpect(jsonPath("$.value").value("V8"));
    }

    @Test
    public void testGetCarSpecificationById() throws Exception {
        CarSpecificationResponseDTO carSpecDto = new CarSpecificationResponseDTO(1, 2, "Engine Type", "V8");

        when(carSpecificationService.getCarSpecificationById(1, 2)).thenReturn(carSpecDto);

        mockMvc.perform(get("/api/car-specifications/cars/{carId}/specs/{specId}", 1, 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.car_id").value(1))
                .andExpect(jsonPath("$.spec_id").value(2))
                .andExpect(jsonPath("$.name").value("Engine Type"))
                .andExpect(jsonPath("$.value").value("V8"));
    }

    @Test
    public void testUpdateCarSpecification() throws Exception {
        CarSpecificationResponseDTO inputDto = new CarSpecificationResponseDTO();
        inputDto.setValue("V12");

        CarSpecificationResponseDTO outputDto = new CarSpecificationResponseDTO(1, 2, "Engine Type", "V12");

        when(carSpecificationService.updateCarSpecification(eq(1), eq(2), any(CarSpecificationResponseDTO.class))).thenReturn(outputDto);

        mockMvc.perform(put("/api/car-specifications/cars/{carId}/specs/{specId}", 1, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.car_id").value(1))
                .andExpect(jsonPath("$.spec_id").value(2))
                .andExpect(jsonPath("$.name").value("Engine Type"))
                .andExpect(jsonPath("$.value").value("V12"));
    }

    @Test
    public void testDeleteCarSpecification() throws Exception {
        doNothing().when(carSpecificationService).deleteCarSpecification(1, 2);

        mockMvc.perform(delete("/api/car-specifications/cars/{carId}/specs/{specId}", 1, 2))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetAllCarSpecifications() throws Exception {
        CarSpecificationResponseDTO carSpec1 = new CarSpecificationResponseDTO(1, 2, "Engine Type", "V8");
        CarSpecificationResponseDTO carSpec2 = new CarSpecificationResponseDTO(1, 3, "Horsepower", "400");

        List<CarSpecificationResponseDTO> carSpecs = Arrays.asList(carSpec1, carSpec2);

        when(carSpecificationService.getAllCarSpecifications()).thenReturn(carSpecs);

        mockMvc.perform(get("/api/car-specifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].car_id").value(1))
                .andExpect(jsonPath("$[0].spec_id").value(2))
                .andExpect(jsonPath("$[0].name").value("Engine Type"))
                .andExpect(jsonPath("$[0].value").value("V8"))
                .andExpect(jsonPath("$[1].car_id").value(1))
                .andExpect(jsonPath("$[1].spec_id").value(3))
                .andExpect(jsonPath("$[1].name").value("Horsepower"))
                .andExpect(jsonPath("$[1].value").value("400"));
    }

    @Test
    public void testGetCarSpecificationById_NotFound() throws Exception {
        when(carSpecificationService.getCarSpecificationById(1, 999))
                .thenThrow(new ResourceNotFoundException("CarSpecification not found with carId 1 and specId 999"));

        mockMvc.perform(get("/api/car-specifications/cars/{carId}/specs/{specId}", 1, 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("CarSpecification not found with carId 1 and specId 999"));
    }

    // Helper method to convert objects to JSON string
    private String asJsonString(final Object obj) {
        try {
            return objectMapper
                    .findAndRegisterModules()
                    .writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
