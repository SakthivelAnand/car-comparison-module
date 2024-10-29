package com.car360.carcomparison.car_comparison_module.controller;

import com.car360.carcomparison.car_comparison_module.dto.CompareRequestDTO;
import com.car360.carcomparison.car_comparison_module.dto.CompareResponseDTO;
import com.car360.carcomparison.car_comparison_module.service.CarComparisonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

/**
 * Controller for handling car comparison-related endpoints.
 */
@RestController
@RequestMapping("/api/comparisons")
@CrossOrigin(origins = "*") // Adjust origins as needed
public class CarComparisonController {

    @Autowired
    private CarComparisonService comparisonService;

    /**
     * Compare Cars
     * Endpoint: POST /api/comparisons/compare
     * Description: Accepts a list of car IDs to compare, returning a table with only differences (if requested).
     *
     * @param compareRequest The comparison request payload.
     * @return ResponseEntity containing the comparison results.
     */
    @PostMapping("/compare")
    public ResponseEntity<CompareResponseDTO> compareCars(@Valid @RequestBody CompareRequestDTO compareRequest) {
        CompareResponseDTO responseDTO = comparisonService.compareCars(compareRequest);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Existing endpoints...

}
