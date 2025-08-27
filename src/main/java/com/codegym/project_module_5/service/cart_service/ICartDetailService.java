package com.codegym.project_module_5.service.cart_service;

import com.codegym.project_module_5.model.cart_model.CartItem;
import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.service.general_service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface ICartDetailService extends IGeneralService<CartItem> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndDish(User user, Dish dish);
    Optional<CartItem> findByUserIdAndDishId(Long userId, Long dishId);
    Long countByUserId(Long userId);
    Optional<Integer> getTotalQuantityByUserId(Long userId);
    Optional<Double> getTotalPriceByUserId(Long userId);
    List<CartItem> findByUserId(Long userId);
    void deleteByUser(User user);
    void deleteByUserId(Long userId);
    boolean existsByUserIdAndDishId(Long userId, Long dishId);
    List<CartItem> findByUserIdAndCategoryId(Long userId, Long categoryId);
    List<CartItem> findByUserIdAndRestaurantId(Long userId, Long restaurantId);

}
