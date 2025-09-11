package com.codegym.project_module_5.repository.evaluate_repository;

import com.codegym.project_module_5.model.evaluate_model.Evaluate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEvaluateRepository extends JpaRepository<Evaluate, Long> {
}
