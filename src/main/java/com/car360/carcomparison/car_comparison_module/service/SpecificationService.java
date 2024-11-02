package com.car360.carcomparison.car_comparison_module.service;

import com.car360.carcomparison.car_comparison_module.dto.SpecificationDTO;

import java.util.List;

public interface SpecificationService {

    SpecificationDTO createSpecification(SpecificationDTO specificationDTO);

    SpecificationDTO getSpecificationById(Integer specId);

    SpecificationDTO updateSpecification(Integer specId, SpecificationDTO specificationDTO);

    void deleteSpecification(Integer specId);

    List<SpecificationDTO> getAllSpecifications();
}

