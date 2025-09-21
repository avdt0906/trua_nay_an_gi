package com.codegym.project_module_5.service.impl.restaurant_service_impl;

import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.repository.restaurant_repository.IDishRepository;
import com.codegym.project_module_5.service.restaurant_service.IDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<Dish> findAll(Pageable pageable) {
        return dishRepository.findAll(pageable);
    }

    @Override
    public Page<Dish> findAllByRestaurantId(Long restaurantId, Pageable pageable) {
        return dishRepository.findAllByRestaurantId(restaurantId, pageable);
    }

    @Override
    public Page<Dish> findAllByRestaurantIdAndNameContainingIgnoreCase(Long restaurantId, String name, Pageable pageable) {
        return dishRepository.findAllByRestaurantIdAndNameContainingIgnoreCase(restaurantId, name, pageable);
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
    public List<Dish> findTop8ByOrderByDiscountDesc() {
        return dishRepository.findTop8ByOrderByDiscountDesc();
    }
    @Override
    public List<Dish> findByRestaurantId(Long restaurantId) {
        return dishRepository.findByRestaurant_Id(restaurantId);
    }
    
    @Override
    public List<Dish> findByCategoryIdAndRestaurantApproved(Long categoryId) {
        return dishRepository.findByCategoryIdAndRestaurantApproved(categoryId);
    }
    
    @Override
    public Page<Dish> findByCategoryIdAndRestaurantApproved(Long categoryId, Pageable pageable) {
        return dishRepository.findByCategoryIdAndRestaurantApproved(categoryId, pageable);
    }
    
    @Override
    public List<Dish> findBestPriceDishes(Pageable pageable) {
        return dishRepository.findBestPriceDishes(pageable);
    }
    
    @Override
    public List<Dish> findHotPickDishes(Pageable pageable) {
        return dishRepository.findHotPickDishes(pageable);
    }
    
    @Override
    public List<Dish> findNearbyDishes(Pageable pageable) {
        return dishRepository.findNearbyDishes(pageable);
    }

    public Page<Dish> search(String keyword, Pageable pageable) {
        return dishRepository.search(keyword, pageable);
    }
}
