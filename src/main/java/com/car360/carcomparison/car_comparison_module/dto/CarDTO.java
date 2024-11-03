package com.car360.carcomparison.car_comparison_module.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CarDTO {

    @NotBlank(message = "Car name is required.")
    private String name;

    @NotBlank(message = "Brand is required.")
    private String brand;

    @NotNull(message = "Model year is required.")
    private Integer modelYear;

    @NotBlank(message = "Category is required.")
    private String category; // e.g., SUV, Sedan, etc.

    @NotNull(message = "Price range is required.")
    private Integer priceRange; // For quick price band lookup

    private String imageUrl;

    private String description;

    // Constructors
    public CarDTO() {
    }

    public CarDTO(String name, String brand, Integer modelYear, String category, Integer priceRange, String imageUrl, String description) {
        this.name = name;
        this.brand = brand;
        this.modelYear = modelYear;
        this.category = category;
        this.priceRange = priceRange;
        this.imageUrl = imageUrl;
        this.description = description;
    }
}
