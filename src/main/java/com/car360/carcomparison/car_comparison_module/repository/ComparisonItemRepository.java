package com.car360.carcomparison.car_comparison_module.repository;

import com.car360.carcomparison.car_comparison_module.model.ComparisonItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComparisonItemRepository extends JpaRepository<ComparisonItem, Long> {
}

