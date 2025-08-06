package com.codegym.project_module_5.repository;

import com.codegym.project_module_5.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface ICategoryRepository extends CrudRepository<Category, Long> {
}
