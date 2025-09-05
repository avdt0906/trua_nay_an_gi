package com.codegym.project_module_5.repository.restaurant_repository;

import com.codegym.project_module_5.model.restaurant_model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
}
