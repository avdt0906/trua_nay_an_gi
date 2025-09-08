package com.codegym.project_module_5.service.restaurant_service;

import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.service.general_service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IDishService extends IGeneralService<Dish> {
    Page<Dish> findAll(Pageable pageable);

    Iterable<Dish> findAllByRestaurantId(Long restaurantId);

    Iterable<Dish> findAllByRestaurantIdAndNameContainingIgnoreCase(Long restaurantId, String name);

    Iterable<Dish> findAllAvailableDishes();

    Iterable<Dish> searchAvailableDishesByName(String name);

    List<Dish> findSimilarDishesByCategory(Long categoryId, Long excludeDishId);

    List<Dish> findPopularDishesByRestaurant(Long restaurantId, Long excludeDishId);

    List<Dish> findByRestaurantId(Long restaurantId);

    List<Dish> findTop8ByOrderByDiscountDesc();
    
    List<Dish> findByCategoryIdAndRestaurantApproved(Long categoryId);
    Page<Dish> findByCategoryIdAndRestaurantApproved(Long categoryId, Pageable pageable);
    
    List<Dish> findBestPriceDishes(Pageable pageable);
    
    List<Dish> findHotPickDishes(Pageable pageable);
    
    List<Dish> findNearbyDishes(Pageable pageable);
}
