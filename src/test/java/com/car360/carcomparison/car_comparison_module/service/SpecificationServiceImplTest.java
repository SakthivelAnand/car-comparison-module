package com.car360.carcomparison.car_comparison_module.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import com.car360.carcomparison.car_comparison_module.dto.SpecificationDTO;
import com.car360.carcomparison.car_comparison_module.exception.ResourceNotFoundException;
import com.car360.carcomparison.car_comparison_module.mapper.CommonMapper;
import com.car360.carcomparison.car_comparison_module.model.DataType;
import com.car360.carcomparison.car_comparison_module.model.Specification;
import com.car360.carcomparison.car_comparison_module.repository.SpecificationRepository;
import com.car360.carcomparison.car_comparison_module.service.impl.SpecificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class SpecificationServiceImplTest {

    @Mock
    private SpecificationRepository specificationRepository;

    @Mock
    private CommonMapper commonMapper;

    @InjectMocks
    private SpecificationServiceImpl specificationService;

    private Specification specification;
    private SpecificationDTO specificationDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        specification = new Specification();
        specification.setSpecId(1);
        specification.setName("Torque");
        specification.setDataType(DataType.STRING);

        specificationDTO = new SpecificationDTO();
        specificationDTO.setSpecId(1);
        specificationDTO.setName("Torque");
        specificationDTO.setDataType(DataType.STRING);
    }

    @Test
    void testCreateSpecification() {
        when(commonMapper.toSpecificationEntity(any(SpecificationDTO.class))).thenReturn(specification);
        when(specificationRepository.save(any(Specification.class))).thenReturn(specification);
        when(commonMapper.toSpecificationDTO(any(Specification.class))).thenReturn(specificationDTO);

        SpecificationDTO result = specificationService.createSpecification(specificationDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Torque");
        verify(specificationRepository, times(1)).save(specification);
    }

    @Test
    void testGetSpecificationById() {
        when(specificationRepository.findById(anyInt())).thenReturn(Optional.of(specification));
        when(commonMapper.toSpecificationDTO(any(Specification.class))).thenReturn(specificationDTO);

        SpecificationDTO result = specificationService.getSpecificationById(1);

        assertThat(result).isNotNull();
        assertThat(result.getSpecId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Torque");
        verify(specificationRepository, times(1)).findById(1);
    }

    @Test
    void testGetSpecificationById_NotFound() {
        when(specificationRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            specificationService.getSpecificationById(1);
        });

        assertThat(exception.getMessage()).isEqualTo("Specification not found with id 1");
        verify(specificationRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateSpecification() {
        when(specificationRepository.findById(anyInt())).thenReturn(Optional.of(specification));

        SpecificationDTO updatedSpecificationDTO = new SpecificationDTO();
        updatedSpecificationDTO.setName("Updated Torque");
        updatedSpecificationDTO.setDataType(DataType.STRING);

        when(specificationRepository.save(any(Specification.class))).thenReturn(specification);
        when(commonMapper.toSpecificationDTO(any(Specification.class))).thenReturn(updatedSpecificationDTO);
        SpecificationDTO result = specificationService.updateSpecification(1, updatedSpecificationDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Updated Torque");
        verify(specificationRepository, times(1)).save(specification);
    }

    @Test
    void testUpdateSpecification_NotFound() {
        when(specificationRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            specificationService.updateSpecification(1, specificationDTO);
        });

        assertThat(exception.getMessage()).isEqualTo("Specification not found with id 1");
        verify(specificationRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteSpecification() {
        when(specificationRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(specificationRepository).deleteById(anyInt());

        specificationService.deleteSpecification(1);

        verify(specificationRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteSpecification_NotFound() {
        when(specificationRepository.existsById(anyInt())).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            specificationService.deleteSpecification(1);
        });

        assertThat(exception.getMessage()).isEqualTo("Specification not found with id 1");
        verify(specificationRepository, times(1)).existsById(1);
    }

    @Test
    void testGetAllSpecifications() {
        Specification spec2 = new Specification();
        spec2.setSpecId(2);
        spec2.setName("Horsepower");
        spec2.setDataType(DataType.INTEGER);

        SpecificationDTO specDto2 = new SpecificationDTO();
        specDto2.setSpecId(2);
        specDto2.setName("Horsepower");
        specDto2.setDataType(DataType.INTEGER);

        when(specificationRepository.findAll()).thenReturn(Arrays.asList(specification, spec2));
        when(commonMapper.toSpecificationDTO(specification)).thenReturn(specificationDTO);
        when(commonMapper.toSpecificationDTO(spec2)).thenReturn(specDto2);

        List<SpecificationDTO> result = specificationService.getAllSpecifications();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Torque");
        assertThat(result.get(1).getName()).isEqualTo("Horsepower");
        verify(specificationRepository, times(1)).findAll();
    }
}
