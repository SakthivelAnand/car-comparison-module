package com.car360.carcomparison.car_comparison_module.repository;

import com.car360.carcomparison.car_comparison_module.model.CarSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarSpecificationRepository extends JpaRepository<CarSpecification, Integer> {

    @Query("SELECT cs FROM CarSpecification cs WHERE cs.car.carId = :baseCarId OR cs.car.carId IN :compareCarIds")
    List<CarSpecification> findSpecifications(@Param("baseCarId") Integer baseCarId, @Param("compareCarIds") List<Integer> compareCarIds);

}

