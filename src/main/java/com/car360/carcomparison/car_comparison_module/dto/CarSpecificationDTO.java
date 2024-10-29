package com.car360.carcomparison.car_comparison_module.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CarSpecificationDTO {

    @JsonProperty("specification_name")
    private String specificationName;

    @JsonProperty("value")
    private String value;

    // Constructors
    public CarSpecificationDTO() {}

    public CarSpecificationDTO(String specificationName, String value) {
        this.specificationName = specificationName;
        this.value = value;
    }

    // Getters and Setters
    public String getSpecificationName() {
        return specificationName;
    }

    public void setSpecificationName(String specificationName) {
        this.specificationName = specificationName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

