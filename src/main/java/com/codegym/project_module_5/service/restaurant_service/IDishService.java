package com.codegym.project_module_5.service.restaurant_service;

import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.service.general_service.IGeneralService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IDishService extends IGeneralService<Dish> {
    Iterable<Dish> findAllByRestaurantId(Long restaurantId);

    Iterable<Dish> findAllByRestaurantIdAndNameContainingIgnoreCase(Long restaurantId, String name);

    Iterable<Dish> findAllAvailableDishes();

    Iterable<Dish> searchAvailableDishesByName(String name);

    List<Dish> findSimilarDishesByCategory(Long categoryId, Long excludeDishId);

    List<Dish> findPopularDishesByRestaurant(Long restaurantId, Long excludeDishId);

    List<Dish> findByRestaurantId(Long restaurantId);
}
