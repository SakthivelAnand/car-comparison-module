package com.car360.carcomparison.car_comparison_module.repository;

import com.car360.carcomparison.car_comparison_module.model.Comparison;
import com.car360.carcomparison.car_comparison_module.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComparisonRepository extends JpaRepository<Comparison, Long> {
    List<Comparison> findByUserOrderByTimestampDesc(User user);
}
