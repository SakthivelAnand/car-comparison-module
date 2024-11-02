package com.car360.carcomparison.car_comparison_module.dto;

import com.car360.carcomparison.car_comparison_module.model.DataType;
import lombok.Data;

@Data
public class SpecificationDTO {

    private Integer specId;
    private String name;
    private DataType dataType;

    public SpecificationDTO() {}

    public SpecificationDTO(Integer specId, String name, DataType dataType) {
        this.specId = specId;
        this.name = name;
        this.dataType = dataType;
    }
}

