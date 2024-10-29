package com.car360.carcomparison.car_comparison_module.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "CarComparisons", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_base_car_id", columnList = "base_car_id")
})
public class CarComparison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comparison_id")
    private Integer comparisonId;

    @NotNull(message = "User ID is required.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Base Car ID is required.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_car_id", nullable = false)
    private Car baseCar;

    @ManyToMany
    @JoinTable(
            name = "CarComparisons_Cars",
            joinColumns = @JoinColumn(name = "comparison_id"),
            inverseJoinColumns = @JoinColumn(name = "car_id")
    )
    private Set<Car> compareCars = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructors

    public CarComparison() {
    }

    public CarComparison(User user, Car baseCar, Set<Car> compareCars) {
        this.user = user;
        this.baseCar = baseCar;
        this.compareCars = compareCars;
        this.createdAt = LocalDateTime.now();
    }
}
