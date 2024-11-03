package com.car360.carcomparison.car_comparison_module.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Car", schema = "car360", indexes = {
        @Index(name = "idx_brand", columnList = "brand"),
        @Index(name = "idx_category", columnList = "category"),
        @Index(name = "idx_price_range", columnList = "price_range"),
        @Index(name = "idx_model_year", columnList = "model_year")
})
@ToString(exclude = "carSpecifications")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Integer carId;

    @NotBlank(message = "Car name is required.")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Brand is required.")
    @Column(name = "brand", nullable = false, length = 50)
    private String brand;

    @NotNull(message = "Model year is required.")
    @Column(name = "model_year", nullable = false)
    private Integer modelYear;

    @NotBlank(message = "Category is required.")
    @Column(name = "category", nullable = false, length = 50)
    private String category; // e.g., SUV, Sedan, etc.

    @NotNull(message = "Price range is required.")
    @Column(name = "price_range", nullable = false)
    private Integer priceRange; // For quick price band lookup

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<CarSpecification> carSpecifications;

    public Car() {
    }

    public Car(String name, String brand, Integer modelYear, String category, Integer priceRange, String imageUrl, String description) {
        this.name = name;
        this.brand = brand;
        this.modelYear = modelYear;
        this.category = category;
        this.priceRange = priceRange;
        this.imageUrl = imageUrl;
        this.description = description;
    }

}
