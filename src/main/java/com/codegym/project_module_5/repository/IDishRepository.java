package com.codegym.project_module_5.repository;

import com.codegym.project_module_5.model.Dish;
import org.springframework.data.repository.CrudRepository;

public interface IDishRepository extends CrudRepository<Dish, Long> {
}
