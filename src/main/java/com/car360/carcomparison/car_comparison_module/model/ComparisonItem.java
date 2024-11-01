package com.car360.carcomparison.car_comparison_module.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "ComparisonItems")
@ToString(exclude = {"comparison", "car"})
public class ComparisonItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comparison_id", nullable = false)
    @JsonIgnore
    private Comparison comparison;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    public ComparisonItem() {}


    public ComparisonItem(Comparison comparison, Car car) {
        this.comparison = comparison;
        this.car = car;
    }
}

