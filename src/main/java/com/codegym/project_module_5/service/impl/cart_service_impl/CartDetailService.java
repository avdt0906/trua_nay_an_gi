package com.codegym.project_module_5.service.impl.cart_service_impl;

import com.codegym.project_module_5.model.cart_model.CartItem;
import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.repository.cart_repository.ICartDetailRepository;
import com.codegym.project_module_5.service.cart_service.ICartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartDetailService implements ICartDetailService {
    @Autowired
    private ICartDetailRepository cartDetailRepository;

    @Override
    public List<CartItem> findByUser(User user) {
        return cartDetailRepository.findByUser(user);
    }

    @Override
    public Optional<CartItem> findByUserAndDish(User user, Dish dish) {
        return cartDetailRepository.findByUserAndDish(user, dish);
    }

    @Override
    public Optional<CartItem> findByUserIdAndDishId(Long userId, Long dishId) {
        return cartDetailRepository.findByUserIdAndDishId(userId, dishId);
    }

    @Override
    public Long countByUserId(Long userId) {
        return cartDetailRepository.countByUserId(userId);
    }

    @Override
    public Optional<Integer> getTotalQuantityByUserId(Long userId) {
        return cartDetailRepository.getTotalQuantityByUserId(userId);
    }

    @Override
    public Optional<Double> getTotalPriceByUserId(Long userId) {
        return cartDetailRepository.getTotalPriceByUserId(userId);
    }

    @Override
    public List<CartItem> findByUserId(Long userId) {
        return cartDetailRepository.findByUserId(userId);
    }

    @Override
    public void deleteByUser(User user) {
        cartDetailRepository.deleteByUser(user);
    }

    @Override
    public void deleteByUserId(Long userId) {
        cartDetailRepository.deleteByUserId(userId);
    }

    @Override
    public boolean existsByUserIdAndDishId(Long userId, Long dishId) {
        return cartDetailRepository.existsByUserIdAndDishId(userId, dishId);
    }

    @Override
    public List<CartItem> findByUserIdAndCategoryId(Long userId, Long categoryId) {
        return cartDetailRepository.findByUserIdAndCategoryId(userId, categoryId);
    }

    @Override
    public List<CartItem> findByUserIdAndRestaurantId(Long userId, Long restaurantId) {
        return cartDetailRepository.findByUserIdAndRestaurantId(userId, restaurantId);
    }

    @Override
    public Iterable<CartItem> findAll() {
        return cartDetailRepository.findAll();
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        return cartDetailRepository.findById(id);
    }

    @Override
    public void save(CartItem cartItem) {
        cartDetailRepository.save(cartItem);
    }

    @Override
    public void delete(Long id) {
        cartDetailRepository.deleteById(id);
    }
}
