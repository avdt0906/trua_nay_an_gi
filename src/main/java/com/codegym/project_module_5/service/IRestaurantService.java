package com.codegym.project_module_5.service;

import com.codegym.project_module_5.model.Restaurant;

import java.util.Optional;

public interface IRestaurantService extends IGeneralService<Restaurant> {
    Optional<Restaurant> findByUsername(String username);

    boolean approveRestaurant(Long restaurantId);

    boolean rejectRestaurant(Long restaurantId);
    
    boolean toggleRestaurantApproval(Long restaurantId);
}
