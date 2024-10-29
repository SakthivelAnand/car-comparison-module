package com.car360.carcomparison.car_comparison_module.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "CarSpecifications", indexes = {
        @Index(name = "idx_car_id", columnList = "car_id"),
        @Index(name = "idx_spec_id", columnList = "spec_id")
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CarSpecification {

    @EmbeddedId
    private CarSpecificationId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("carId")
    @JoinColumn(name = "car_id", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("specId")
    @JoinColumn(name = "spec_id", nullable = false, insertable = false, updatable = false)
    private Specification specification;

    @Column(name = "value", nullable = false, length = 255)
    private String value;

    // Constructors
    public CarSpecification() {}

    public CarSpecification(CarSpecificationId id, Car car, Specification specification, String value) {
        this.id = id;
        this.car = car;
        this.specification = specification;
        this.value = value;
    }
}
