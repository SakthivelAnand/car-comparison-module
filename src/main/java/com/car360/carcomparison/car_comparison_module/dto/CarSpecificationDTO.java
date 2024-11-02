package com.car360.carcomparison.car_comparison_module.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CarSpecificationDTO {

    @JsonProperty("specification_name")
    private String specificationName;

    @JsonProperty("value")
    private String value;

    public CarSpecificationDTO() {}

    public CarSpecificationDTO(String specificationName, String value) {
        this.specificationName = specificationName;
        this.value = value;
    }
}

