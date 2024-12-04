package com.coms3090.calculator.repository;

import com.coms3090.calculator.entity.Calculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculationRepository extends JpaRepository<Calculation, Long> {
}
