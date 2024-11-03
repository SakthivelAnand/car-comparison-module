package com.car360.carcomparison.car_comparison_module.controller;


import com.car360.carcomparison.car_comparison_module.dto.SpecificationDTO;
import com.car360.carcomparison.car_comparison_module.exception.GlobalExceptionHandler;
import com.car360.carcomparison.car_comparison_module.exception.ResourceNotFoundException;
import com.car360.carcomparison.car_comparison_module.model.DataType;
import com.car360.carcomparison.car_comparison_module.service.SpecificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SpecificationController.class)
public class SpecificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpecificationService specificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateSpecification() throws Exception {
        SpecificationDTO inputDto = new SpecificationDTO();
        inputDto.setName("Engine Type");
        inputDto.setDataType(DataType.STRING);

        SpecificationDTO outputDto = new SpecificationDTO();
        outputDto.setSpecId(1);
        outputDto.setName("Engine Type");
        outputDto.setDataType(DataType.STRING);

        when(specificationService.createSpecification(any(SpecificationDTO.class))).thenReturn(outputDto);

        mockMvc.perform(post("/api/specifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.spec_id").value(1))
                .andExpect(jsonPath("$.name").value("Engine Type"))
                .andExpect(jsonPath("$.data_type").value("STRING"));
    }

    @Test
    public void testGetSpecificationById() throws Exception {
        SpecificationDTO specificationDTO = new SpecificationDTO(1, "Engine Type", DataType.STRING);

        when(specificationService.getSpecificationById(1)).thenReturn(specificationDTO);

        mockMvc.perform(get("/api/specifications/{specId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.spec_id").value(1))
                .andExpect(jsonPath("$.name").value("Engine Type"))
                .andExpect(jsonPath("$.data_type").value("STRING"));
    }

    @Test
    public void testUpdateSpecification() throws Exception {
        SpecificationDTO inputDto = new SpecificationDTO();
        inputDto.setName("Updated Engine Type");
        inputDto.setDataType(DataType.STRING);

        SpecificationDTO outputDto = new SpecificationDTO(1, "Updated Engine Type", DataType.STRING);

        when(specificationService.updateSpecification(eq(1), any(SpecificationDTO.class))).thenReturn(outputDto);

        mockMvc.perform(put("/api/specifications/{specId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.spec_id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Engine Type"))
                .andExpect(jsonPath("$.data_type").value("STRING"));
    }

    @Test
    public void testDeleteSpecification() throws Exception {
        doNothing().when(specificationService).deleteSpecification(1);

        mockMvc.perform(delete("/api/specifications/{specId}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetAllSpecifications() throws Exception {
        SpecificationDTO spec1 = new SpecificationDTO(1, "Engine Type", DataType.STRING);
        SpecificationDTO spec2 = new SpecificationDTO(2, "Horsepower", DataType.INTEGER);

        List<SpecificationDTO> specs = Arrays.asList(spec1, spec2);

        when(specificationService.getAllSpecifications()).thenReturn(specs);

        mockMvc.perform(get("/api/specifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].spec_id").value(1))
                .andExpect(jsonPath("$[0].name").value("Engine Type"))
                .andExpect(jsonPath("$[0].data_type").value("STRING"))
                .andExpect(jsonPath("$[1].spec_id").value(2))
                .andExpect(jsonPath("$[1].name").value("Horsepower"))
                .andExpect(jsonPath("$[1].data_type").value("INTEGER"));
    }

    @Test
    public void testGetSpecificationById_NotFound() throws Exception {
        when(specificationService.getSpecificationById(999)).thenThrow(new ResourceNotFoundException("Specification not found with id 999"));

        mockMvc.perform(get("/api/specifications/{specId}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Specification not found with id 999"));
    }

    // Helper method to convert objects to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper()
                    .findAndRegisterModules()
                    .writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
