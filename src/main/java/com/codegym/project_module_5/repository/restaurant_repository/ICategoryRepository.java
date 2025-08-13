package com.codegym.project_module_5.repository.restaurant_repository;

import com.codegym.project_module_5.model.restaurant_model.Category;
import org.springframework.data.repository.CrudRepository;

public interface ICategoryRepository extends CrudRepository<Category, Long> {
}
