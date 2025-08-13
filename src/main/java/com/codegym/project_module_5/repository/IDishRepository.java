package com.codegym.project_module_5.repository;

import com.codegym.project_module_5.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IDishRepository extends CrudRepository<Dish, Long> {
    Iterable<Dish> findAllByRestaurant_Id(Long restaurantId);

    Iterable<Dish> findAllByRestaurantIdAndNameContainingIgnoreCase(Long restaurantId, String name);

}
