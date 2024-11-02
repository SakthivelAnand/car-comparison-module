package com.car360.carcomparison.car_comparison_module.repository;

import com.car360.carcomparison.car_comparison_module.model.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecificationRepository extends JpaRepository<Specification, Integer> {
    // Additional query methods if needed
}

