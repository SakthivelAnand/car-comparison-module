package com.car360.carcomparison.car_comparison_module.controller;

import com.car360.carcomparison.car_comparison_module.dto.SpecificationDTO;
import com.car360.carcomparison.car_comparison_module.service.SpecificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specifications")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    @PostMapping
    public ResponseEntity<SpecificationDTO> createSpecification(@Valid @RequestBody SpecificationDTO specificationDTO) {
        SpecificationDTO createdSpecification = specificationService.createSpecification(specificationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSpecification);
    }

    @GetMapping("/{specId}")
    public ResponseEntity<SpecificationDTO> getSpecificationById(@PathVariable Integer specId) {
        SpecificationDTO specificationDTO = specificationService.getSpecificationById(specId);
        return ResponseEntity.ok(specificationDTO);
    }

    @PutMapping("/{specId}")
    public ResponseEntity<SpecificationDTO> updateSpecification(
            @PathVariable Integer specId,
            @Valid @RequestBody SpecificationDTO specificationDTO) {
        SpecificationDTO updatedSpecification = specificationService.updateSpecification(specId, specificationDTO);
        return ResponseEntity.ok(updatedSpecification);
    }

    @DeleteMapping("/{specId}")
    public ResponseEntity<Void> deleteSpecification(@PathVariable Integer specId) {
        specificationService.deleteSpecification(specId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SpecificationDTO>> getAllSpecifications() {
        List<SpecificationDTO> specifications = specificationService.getAllSpecifications();
        return ResponseEntity.ok(specifications);
    }
}

