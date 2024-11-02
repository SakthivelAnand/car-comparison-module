package com.car360.carcomparison.car_comparison_module.controller;

import com.car360.carcomparison.car_comparison_module.dto.CarSpecificationResponseDTO;
import com.car360.carcomparison.car_comparison_module.service.CarSpecificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/car-specifications")
public class CarSpecificationController {

    @Autowired
    private CarSpecificationService carSpecificationService;

    @PostMapping
    public ResponseEntity<CarSpecificationResponseDTO> createCarSpecification(
            @Valid @RequestBody CarSpecificationResponseDTO carSpecificationDTO) {
        CarSpecificationResponseDTO createdCarSpecification = carSpecificationService.createCarSpecification(carSpecificationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCarSpecification);
    }

    @GetMapping("/cars/{carId}/specs/{specId}")
    public ResponseEntity<CarSpecificationResponseDTO> getCarSpecificationById(
            @PathVariable Integer carId,
            @PathVariable Integer specId) {
        CarSpecificationResponseDTO carSpecificationDTO = carSpecificationService.getCarSpecificationById(carId, specId);
        return ResponseEntity.ok(carSpecificationDTO);
    }

    @PutMapping("/cars/{carId}/specs/{specId}")
    public ResponseEntity<CarSpecificationResponseDTO> updateCarSpecification(
            @PathVariable Integer carId,
            @PathVariable Integer specId,
            @Valid @RequestBody CarSpecificationResponseDTO carSpecificationDTO) {
        CarSpecificationResponseDTO updatedCarSpecification = carSpecificationService.updateCarSpecification(carId, specId, carSpecificationDTO);
        return ResponseEntity.ok(updatedCarSpecification);
    }

    @DeleteMapping("/cars/{carId}/specs/{specId}")
    public ResponseEntity<Void> deleteCarSpecification(
            @PathVariable Integer carId,
            @PathVariable Integer specId) {
        carSpecificationService.deleteCarSpecification(carId, specId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CarSpecificationResponseDTO>> getAllCarSpecifications() {
        List<CarSpecificationResponseDTO> carSpecifications = carSpecificationService.getAllCarSpecifications();
        return ResponseEntity.ok(carSpecifications);
    }
}

