package com.codegym.project_module_5.service.impl.dish_detail_service_impl;

import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.repository.dish_detail_repository.IDishDetailRepository;
import com.codegym.project_module_5.service.dish_detail_service.IDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishDetailService implements IDishService {
    @Autowired
    private IDishDetailRepository iDishDetailRepository;

    @Override
    public Iterable<Dish> findAll() {
        return iDishDetailRepository.findAll();
    }

    @Override
    public Optional<Dish> findById(Long id) {
        return iDishDetailRepository.findById(id);
    }

    @Override
    public void save(Dish dish) {
        iDishDetailRepository.save(dish);
    }

    @Override
    public List<Dish> findSimilarDishesByCategory(Long categoryId, Long excludeDishId) {
        return iDishDetailRepository.findSimilarDishesByCategory(categoryId, excludeDishId);
    }

    @Override
    public List<Dish> findPopularDishesByRestaurant(Long restaurantId, Long excludeDishId) {
        return iDishDetailRepository.findPopularDishesByRestaurant(restaurantId, excludeDishId);
    }
}
