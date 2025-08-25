package com.codegym.project_module_5.service.restaurant_service;

import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.model.restaurant_model.Coupon;
import com.codegym.project_module_5.model.dto.request.RestaurantRegisterRequest;
import com.codegym.project_module_5.service.general_service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface IRestaurantService extends IGeneralService<Restaurant> {
    Optional<Restaurant> findByUsername(String username);
    Restaurant registerRestaurant(RestaurantRegisterRequest request, String currentUsername);
    List<Coupon> getCouponsByRestaurantId(Long restaurantId);
    Restaurant toggleLockStatus(Long restaurantId);
    List<Restaurant> getPendingApprovalRestaurants();
}
