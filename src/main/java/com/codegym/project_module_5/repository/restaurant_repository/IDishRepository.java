package com.codegym.project_module_5.repository.restaurant_repository;

import com.codegym.project_module_5.model.restaurant_model.Dish;
import org.springframework.data.repository.CrudRepository;


public interface IDishRepository extends CrudRepository<Dish, Long> {
    Iterable<Dish> findAllByRestaurant_Id(Long restaurantId);

    Iterable<Dish> findAllByRestaurantIdAndNameContainingIgnoreCase(Long restaurantId, String name);

}
