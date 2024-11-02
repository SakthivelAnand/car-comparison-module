package com.car360.carcomparison.car_comparison_module.service;

import com.car360.carcomparison.car_comparison_module.dto.CarSpecificationResponseDTO;
import com.car360.carcomparison.car_comparison_module.exception.ResourceNotFoundException;
import com.car360.carcomparison.car_comparison_module.mapper.CommonMapper;
import com.car360.carcomparison.car_comparison_module.model.Car;
import com.car360.carcomparison.car_comparison_module.model.CarSpecification;
import com.car360.carcomparison.car_comparison_module.model.CarSpecificationId;
import com.car360.carcomparison.car_comparison_module.model.Specification;
import com.car360.carcomparison.car_comparison_module.repository.CarRepository;
import com.car360.carcomparison.car_comparison_module.repository.CarSpecificationRepository;
import com.car360.carcomparison.car_comparison_module.repository.SpecificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarSpecificationServiceImpl implements CarSpecificationService {

    @Autowired
    private CarSpecificationRepository carSpecificationRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private SpecificationRepository specificationRepository;

    @Autowired
    private CommonMapper manualMapper;

    @Override
    public CarSpecificationResponseDTO createCarSpecification(CarSpecificationResponseDTO carSpecificationDTO) {
        Integer carId = carSpecificationDTO.getCarId();
        Integer specId = carSpecificationDTO.getSpecId();

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id " + carId));

        Specification specification = specificationRepository.findById(specId)
                .orElseThrow(() -> new ResourceNotFoundException("Specification not found with id " + specId));

        CarSpecification carSpecification = manualMapper.toCarSpecificationEntity(carSpecificationDTO, car, specification);
        CarSpecification savedCarSpecification = carSpecificationRepository.save(carSpecification);
        return manualMapper.toCarSpecificationResponseDTO(savedCarSpecification);
    }

    @Override
    public CarSpecificationResponseDTO getCarSpecificationById(Integer carId, Integer specId) {
        CarSpecificationId id = new CarSpecificationId(carId, specId);
        CarSpecification carSpecification = carSpecificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CarSpecification not found with carId " + carId + " and specId " + specId));
        return manualMapper.toCarSpecificationResponseDTO(carSpecification);
    }

    @Override
    public CarSpecificationResponseDTO updateCarSpecification(Integer carId, Integer specId, CarSpecificationResponseDTO carSpecificationDTO) {
        CarSpecificationId id = new CarSpecificationId(carId, specId);
        CarSpecification existingCarSpecification = carSpecificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CarSpecification not found with carId " + carId + " and specId " + specId));

        existingCarSpecification.setValue(carSpecificationDTO.getValue());

        CarSpecification updatedCarSpecification = carSpecificationRepository.save(existingCarSpecification);
        return manualMapper.toCarSpecificationResponseDTO(updatedCarSpecification);
    }

    @Override
    public void deleteCarSpecification(Integer carId, Integer specId) {
        CarSpecificationId id = new CarSpecificationId(carId, specId);
        if (!carSpecificationRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "CarSpecification not found with carId " + carId + " and specId " + specId);
        }
        carSpecificationRepository.deleteById(id);
    }

    @Override
    public List<CarSpecificationResponseDTO> getAllCarSpecifications() {
        return carSpecificationRepository.findAll().stream()
                .map(manualMapper::toCarSpecificationResponseDTO)
                .collect(Collectors.toList());
    }
}

