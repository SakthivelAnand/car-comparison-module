package com.car360.carcomparison.car_comparison_module.controller;

import com.car360.carcomparison.car_comparison_module.dto.CompareRequestDTO;
import com.car360.carcomparison.car_comparison_module.dto.CompareResponseDTO;
import com.car360.carcomparison.car_comparison_module.dto.ComparisonHistoryDTO;
import com.car360.carcomparison.car_comparison_module.model.User;
import com.car360.carcomparison.car_comparison_module.service.CarComparisonService;
import com.car360.carcomparison.car_comparison_module.service.CarService;
import com.car360.carcomparison.car_comparison_module.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Controller for handling car comparison-related endpoints.
 */
@RestController
@RequestMapping("/api/comparisons")
@CrossOrigin(origins = "*") // Adjust origins as needed
public class CarComparisonController {

    @Autowired
    private CarComparisonService comparisonService;

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;

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
        Long userId = compareRequest.getUserId();
        User user = userService.getUserByUserId(userId);

        // Save the comparison history
        List<Integer> allCarIds = new java.util.ArrayList<>();
        allCarIds.add(compareRequest.getBaseCarId());
        allCarIds.addAll(compareRequest.getCompareCarIds());

        comparisonService.saveComparison(user, allCarIds);

        CompareResponseDTO responseDTO = comparisonService.compareCars(compareRequest);

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }


    /**
     * GET /api/comparisons
     *
     * @param userId current user id.
     * @return ResponseEntity containing a list of ComparisonHistoryDTOs.
     */
    @GetMapping
    public ResponseEntity<List<ComparisonHistoryDTO>> getComparisonHistory(@RequestParam("userId") Long userId) {
        User user = userService.getUserByUserId(userId);
        // Retrieve comparison histories
        List<ComparisonHistoryDTO> comparisons = comparisonService.getComparisonHistory(user);

        return ResponseEntity.ok(comparisons);
    }

}
