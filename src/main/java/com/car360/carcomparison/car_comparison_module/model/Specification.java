package com.car360.carcomparison.car_comparison_module.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "Specifications")
public class Specification {

    @Id
    @Column(name = "spec_id")
    private Integer specId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // Assuming DataType is an enum in your code
    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false, length = 10)
    private DataType dataType;


    @OneToMany(mappedBy = "specification", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonBackReference
    private Set<CarSpecification> specifications;

    public Specification(Integer specId, String name, DataType dataType) {
        this.specId = specId;
        this.name = name;
        this.dataType = dataType;
    }

    public Specification() {}

}


