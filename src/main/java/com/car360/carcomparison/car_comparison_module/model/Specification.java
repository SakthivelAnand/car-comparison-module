package com.car360.carcomparison.car_comparison_module.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Specifications")
public class Specification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spec_id")
    private Integer specId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // Assuming DataType is an enum in your code
    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false, length = 10)
    private DataType dataType;


    @OneToMany(mappedBy = "specification", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private Set<CarSpecification> specifications;

    public Specification(Integer specId, String name, DataType dataType) {
        this.specId = specId;
        this.name = name;
        this.dataType = dataType;
    }

    public Specification() {}

}


