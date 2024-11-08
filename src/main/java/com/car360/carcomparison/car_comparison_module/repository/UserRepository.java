package com.car360.carcomparison.car_comparison_module.repository;

import com.car360.carcomparison.car_comparison_module.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserId(Long userId);
}

