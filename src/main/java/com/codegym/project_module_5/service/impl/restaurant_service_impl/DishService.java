package com.codegym.project_module_5.service.impl.restaurant_service_impl;

import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.repository.restaurant_repository.IDishRepository;
import com.codegym.project_module_5.service.restaurant_service.IDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishService implements IDishService {
    @Autowired
    private IDishRepository dishRepository;

    @Override
    public Iterable<Dish> findAll() {
        return dishRepository.findAll();
    }

    @Override
    public Optional<Dish> findById(Long id) {
        return dishRepository.findById(id);
    }

    @Override
    public void save(Dish dish) {
        dishRepository.save(dish);
    }

    @Override
    public void delete(Long id) {
        dishRepository.deleteById(id);
    }

    @Override
    public Iterable<Dish> findAllByRestaurantId(Long restaurantId) {
        return dishRepository.findAllByRestaurantId(restaurantId);
    }

    @Override
    public Iterable<Dish> findAllByRestaurantIdAndNameContainingIgnoreCase(Long restaurantId, String name) {
        return dishRepository.findAllByRestaurantIdAndNameContainingIgnoreCase(restaurantId, name);
    }

    @Override
    public Iterable<Dish> findAllAvailableDishes() {
        return dishRepository.findAllByRestaurantIsApprovedTrueAndRestaurantIsLockedFalse();
    }

    @Override
    public Iterable<Dish> searchAvailableDishesByName(String name) {
        return dishRepository.findAllByNameContainingIgnoreCaseAndRestaurantIsApprovedTrueAndRestaurantIsLockedFalse(name);
    }

    @Override
    public List<Dish> findSimilarDishesByCategory(Long categoryId, Long excludeDishId) {
        return dishRepository.findSimilarDishesByCategory(categoryId, excludeDishId);
    }

    @Override
    public List<Dish> findPopularDishesByRestaurant(Long restaurantId, Long excludeDishId) {
        return dishRepository.findPopularDishesByRestaurant(restaurantId, excludeDishId);
    }

    @Override
    public List<Dish> findByRestaurantId(Long restaurantId) {
        return dishRepository.findByRestaurant_Id(restaurantId);
    }
}
