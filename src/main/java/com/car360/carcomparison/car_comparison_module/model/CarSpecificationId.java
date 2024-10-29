package com.car360.carcomparison.car_comparison_module.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class CarSpecificationId implements Serializable {

    @JoinColumn(name = "car_id", nullable = false)
    private Integer carId;
    @JoinColumn(name = "spec_id", nullable = false)
    private Integer specId;

    public CarSpecificationId() {
    }

    public CarSpecificationId(Integer carId, Integer specId) {
        this.carId = carId;
        this.specId = specId;
    }

}

