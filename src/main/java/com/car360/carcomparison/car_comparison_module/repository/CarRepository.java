package com.car360.carcomparison.car_comparison_module.repository;

import com.car360.carcomparison.car_comparison_module.model.Car;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    List<Car> findByBrandContainingIgnoreCase(String brand);

    List<Car> findByCategoryContainingIgnoreCase(String category);

    List<Car> findByPriceRange(Integer priceRange);

    List<Car> findByModelYear(Integer modelYear);


    /**
     * Fetch a car along with its specifications to avoid LazyInitializationException.
     *
     * @param carId ID of the car.
     * @return Optional containing the car with specifications if found.
     */
    @EntityGraph(attributePaths = {"carSpecifications", "carSpecifications.specification"})
    Optional<Car> findWithSpecificationsByCarId(Integer carId);

    /**
     * Fetch multiple cars along with their specifications.
     *
     * @param carIds List of car IDs.
     * @return List of cars with specifications.
     */
    @EntityGraph(attributePaths = {"carSpecifications", "carSpecifications.specification"})
    List<Car> findAllByCarIdIn(List<Integer> carIds);

}

