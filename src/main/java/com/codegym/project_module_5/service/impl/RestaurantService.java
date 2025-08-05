package com.codegym.project_module_5.service.impl;

import com.codegym.project_module_5.model.Restaurant;
import com.codegym.project_module_5.repository.IRestaurantRepository;
import com.codegym.project_module_5.service.IRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RestaurantService implements IRestaurantService {
    @Autowired
    IRestaurantRepository restaurantRepository;

    @Override
    public Iterable<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id);
    }

    @Override
    public void save(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    @Override
    public Optional<Restaurant> findByUsername(String username) {
        return restaurantRepository.findByUser_Username(username);
    }
}
