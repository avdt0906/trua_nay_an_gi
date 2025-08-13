package com.codegym.project_module_5.service;

import com.codegym.project_module_5.model.Dish;
import org.springframework.stereotype.Service;

@Service
public interface IDishService extends IGeneralService<Dish>{
    Iterable<Dish> findAllByRestaurantId(Long restaurantId);
    Iterable<Dish> findAllByRestaurantIdAndNameContainingIgnoreCase(Long restaurantId, String name);
    Iterable<Dish> findAllAvailableDishes();
    Iterable<Dish> searchAvailableDishesByName(String name);
}
