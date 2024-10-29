package com.car360.carcomparison.car_comparison_module.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CarResponseDTO {

    @JsonProperty("car_id")
    private Integer carId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("model_year")
    private Integer modelYear;

    @JsonProperty("category")
    private String category;

    @JsonProperty("price_range")
    private Integer priceRange;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("description")
    private String description;

    @JsonProperty("car_specifications")
    private List<CarSpecificationDTO> carSpecifications;

    // Constructors
    public CarResponseDTO() {}

    public CarResponseDTO(Integer carId, String name, String brand, Integer modelYear, String category, Integer priceRange, String imageUrl, String description, List<CarSpecificationDTO> carSpecifications) {
        this.carId = carId;
        this.name = name;
        this.brand = brand;
        this.modelYear = modelYear;
        this.category = category;
        this.priceRange = priceRange;
        this.imageUrl = imageUrl;
        this.description = description;
        this.carSpecifications = carSpecifications;
    }


}

