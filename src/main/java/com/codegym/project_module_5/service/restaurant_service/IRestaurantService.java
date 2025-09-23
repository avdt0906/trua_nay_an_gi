package com.codegym.project_module_5.service.restaurant_service;

import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.model.restaurant_model.Coupon;
import com.codegym.project_module_5.model.dto.request.RestaurantRegisterRequest;
import com.codegym.project_module_5.service.general_service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IRestaurantService extends IGeneralService<Restaurant> {
    Optional<Restaurant> findByUsername(String username);
    Restaurant registerRestaurant(RestaurantRegisterRequest request, String currentUsername);
    List<Coupon> getCouponsByRestaurantId(Long restaurantId);
    Restaurant toggleLockStatus(Long restaurantId);
    List<Restaurant> getPendingApprovalRestaurants();
    List<Restaurant> getPartnerRequests();
    void approvePartner(Long restaurantId);
    void rejectPartner(Long restaurantId);
    double calculateTotalRevenue(Long restaurantId);
    Optional<Restaurant> findRestaurantIdByUserId(Long userId);
    Page<Restaurant> findByCouponCodeAndIsAcceptedTrue(Pageable pageable, String couponCode);
    void selectFeaturedDish(Long restaurantId, Long dishId);
}

