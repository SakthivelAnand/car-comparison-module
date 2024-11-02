package com.car360.carcomparison.car_comparison_module.service;

import com.car360.carcomparison.car_comparison_module.dto.SpecificationDTO;
import com.car360.carcomparison.car_comparison_module.exception.ResourceNotFoundException;
import com.car360.carcomparison.car_comparison_module.mapper.CommonMapper;
import com.car360.carcomparison.car_comparison_module.model.Specification;
import com.car360.carcomparison.car_comparison_module.repository.SpecificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecificationRepository specificationRepository;

    @Autowired
    private CommonMapper commonMapper;

    @Override
    public SpecificationDTO createSpecification(SpecificationDTO specificationDTO) {
        Specification specification = commonMapper.toSpecificationEntity(specificationDTO);
        Specification savedSpecification = specificationRepository.save(specification);
        return commonMapper.toSpecificationDTO(savedSpecification);
    }

    @Override
    public SpecificationDTO getSpecificationById(Integer specId) {
        Specification specification = specificationRepository.findById(specId)
                .orElseThrow(() -> new ResourceNotFoundException("Specification not found with id " + specId));
        return commonMapper.toSpecificationDTO(specification);
    }

    @Override
    public SpecificationDTO updateSpecification(Integer specId, SpecificationDTO specificationDTO) {
        Specification existingSpecification = specificationRepository.findById(specId)
                .orElseThrow(() -> new ResourceNotFoundException("Specification not found with id " + specId));

        existingSpecification.setName(specificationDTO.getName());
        existingSpecification.setDataType(specificationDTO.getDataType());

        Specification updatedSpecification = specificationRepository.save(existingSpecification);
        return commonMapper.toSpecificationDTO(updatedSpecification);
    }

    @Override
    public void deleteSpecification(Integer specId) {
        if (!specificationRepository.existsById(specId)) {
            throw new ResourceNotFoundException("Specification not found with id " + specId);
        }
        specificationRepository.deleteById(specId);
    }

    @Override
    public List<SpecificationDTO> getAllSpecifications() {
        return specificationRepository.findAll().stream()
                .map(commonMapper::toSpecificationDTO)
                .collect(Collectors.toList());
    }
}

