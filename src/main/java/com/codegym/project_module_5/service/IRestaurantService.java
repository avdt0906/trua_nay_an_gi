package com.codegym.project_module_5.service;

import com.codegym.project_module_5.model.Restaurant;
import com.codegym.project_module_5.model.dto.request.RestaurantRegisterRequest;

import java.util.Optional;

public interface IRestaurantService extends IGeneralService<Restaurant> {
    Optional<Restaurant> findByUsername(String username);
    Restaurant registerRestaurant(RestaurantRegisterRequest request, String currentUsername);

    boolean approveRestaurant(Long restaurantId);

    boolean rejectRestaurant(Long restaurantId);

    boolean toggleRestaurantApproval(Long restaurantId);
}
