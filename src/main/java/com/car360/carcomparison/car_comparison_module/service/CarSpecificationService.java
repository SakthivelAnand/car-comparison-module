package com.car360.carcomparison.car_comparison_module.service;


import com.car360.carcomparison.car_comparison_module.dto.CarSpecificationResponseDTO;

import java.util.List;

public interface CarSpecificationService {

    CarSpecificationResponseDTO createCarSpecification(CarSpecificationResponseDTO carSpecificationDTO);

    CarSpecificationResponseDTO getCarSpecificationById(Integer carId, Integer specId);

    CarSpecificationResponseDTO updateCarSpecification(Integer carId, Integer specId, CarSpecificationResponseDTO carSpecificationDTO);

    void deleteCarSpecification(Integer carId, Integer specId);

    List<CarSpecificationResponseDTO> getAllCarSpecifications();
}
