package com.car360.carcomparison.car_comparison_module.controller;

import com.car360.carcomparison.car_comparison_module.dto.CompareRequestDTO;
import com.car360.carcomparison.car_comparison_module.dto.CompareResponseDTO;
import com.car360.carcomparison.car_comparison_module.dto.ComparisonHistoryDTO;
import com.car360.carcomparison.car_comparison_module.model.Comparison;
import com.car360.carcomparison.car_comparison_module.model.User;
import com.car360.carcomparison.car_comparison_module.service.CarComparisonService;
import com.car360.carcomparison.car_comparison_module.service.CarService;
import com.car360.carcomparison.car_comparison_module.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarComparisonController.class)
class CarComparisonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarComparisonService comparisonService;

    @MockBean
    private CarService carService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setUsername("testuser");
        // Initialize other user properties as needed
    }

    /**
     * Test the POST /api/comparisons/compare endpoint for a successful comparison.
     */
    @Test
    void testCompareCars_Success() throws Exception {
        // Prepare the request DTO
        CompareRequestDTO compareRequest = new CompareRequestDTO();
        compareRequest.setUserId(mockUser.getUserId());
        compareRequest.setBaseCarId(100);
        compareRequest.setCompareCarIds(Arrays.asList(101, 102));
        compareRequest.setShowOnlyDifferences(true);

        // Prepare the response DTO
        CompareResponseDTO compareResponse = new CompareResponseDTO();
        // Initialize compareResponse properties as needed

        // Mock the UserService
        given(userService.getUserByUserId(mockUser.getUserId())).willReturn(mockUser);

        Comparison mockComparison = mock(Comparison.class); // Create a mock Comparison object
        when(comparisonService.saveComparison(eq(mockUser), ArgumentMatchers.anyList()))
                .thenReturn(mockComparison);

        // Mock the CarComparisonService.compareCaårs
        given(comparisonService.compareCars(any(CompareRequestDTO.class))).willReturn(compareResponse);

        // Perform the POST request
        mockMvc.perform(post("/api/comparisons/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(compareRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        // Add more assertions based on CompareResponseDTO structure
        ;
    }

    /**
     * Test the POST /api/comparisons/compare endpoint with invalid input (e.g., missing userId).
     */
    @Test
    void testCompareCars_InvalidInput() throws Exception {
        // Prepare an invalid request DTO (missing userId)
        CompareRequestDTO compareRequest = new CompareRequestDTO();
        // compareRequest.setUserId(null); // Explicitly omitted
        compareRequest.setBaseCarId(100);
        compareRequest.setCompareCarIds(Arrays.asList(101, 102));
        compareRequest.setShowOnlyDifferences(true);

        // Perform the POST request
        mockMvc.perform(post("/api/comparisons/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(compareRequest)))
                .andExpect(status().isBadRequest());
        // Further assertions can be added based on validation error messages
    }

    /**
     * Test the GET /api/comparisons endpoint for a successful retrieval of comparison history.
     */
    @Test
    void testGetComparisonHistory_Success() throws Exception {
        Long userId = mockUser.getUserId();

        // Prepare the list of comparison histories
        ComparisonHistoryDTO history1 = new ComparisonHistoryDTO();
        // Initialize history1 properties as needed

        ComparisonHistoryDTO history2 = new ComparisonHistoryDTO();
        // Initialize history2 properties as needed

        List<ComparisonHistoryDTO> histories = Arrays.asList(history1, history2);

        // Mock the UserService
        given(userService.getUserByUserId(userId)).willReturn(mockUser);

        // Mock the CarComparisonService.getComparisonHistory
        given(comparisonService.getComparisonHistory(mockUser)).willReturn(histories);

        // Perform the GET request
        mockMvc.perform(get("/api/comparisons")
                .param("userId", userId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
        // Add more assertions based on ComparisonHistoryDTO structure
        ;
    }

    /**
     * Test the GET /api/comparisons endpoint when no history is present.
     */
    @Test
    void testGetComparisonHistory_Empty() throws Exception {
        Long userId = mockUser.getUserId();

        // Mock the UserService
        given(userService.getUserByUserId(userId)).willReturn(mockUser);

        // Mock the CarComparisonService.getComparisonHistory to return empty list
        given(comparisonService.getComparisonHistory(mockUser)).willReturn(Collections.emptyList());

        // Perform the GET request
        mockMvc.perform(get("/api/comparisons")
                .param("userId", userId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * Test the GET /api/comparisons endpoint with invalid userId.
     */
    @Test
    void testGetComparisonHistory_InvalidUser() throws Exception {
        Long invalidUserId = 999L;

        // Mock the UserService to throw an exception or return null
        given(userService.getUserByUserId(invalidUserId)).willThrow(new RuntimeException("User not found"));

        // Perform the GET request
        mockMvc.perform(get("/api/comparisons")
                .param("userId", invalidUserId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
        // Adjust based on how your controller handles exceptions
    }
}
