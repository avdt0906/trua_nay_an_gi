package com.codegym.project_module_5.service.dish_detail_service;

import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.service.general_service.IGeneralService;

import java.util.List;

public interface IDishService extends IGeneralService<Dish> {
    List<Dish> findSimilarDishesByCategory(Long categoryId, Long excludeDishId);
    List<Dish> findPopularDishesByRestaurant(Long restaurantId, Long excludeDishId);
}
