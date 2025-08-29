package com.codegym.project_module_5.service.impl.cart_service_impl;

import com.codegym.project_module_5.model.cart_model.CartItem;
import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.repository.cart_repository.ICartItemRepository;
import com.codegym.project_module_5.service.cart_service.ICartService;
import com.codegym.project_module_5.service.restaurant_service.IDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CartService implements ICartService {

    @Autowired
    private ICartItemRepository cartItemRepository;

    @Autowired
    private IDishService dishService;

    @Override
    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    @Override
    @Transactional
    public void addToCart(User user, Dish dish, int quantity) {
        List<CartItem> currentItems = cartItemRepository.findByUser(user);
        if (!currentItems.isEmpty()) {
            Long existingRestaurantId = currentItems.get(0).getDish().getRestaurant().getId();
            Long newRestaurantId = dish.getRestaurant().getId();
            if (!existingRestaurantId.equals(newRestaurantId)) {
                throw new IllegalArgumentException("Chỉ được đặt món từ một nhà hàng trong mỗi đơn");
            }
        }
        Optional<CartItem> existingItem = cartItemRepository.findByUserAndDish(user, dish);

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem newItem = new CartItem(null, user, dish, quantity);
            cartItemRepository.save(newItem);
        }
    }

    @Override
    @Transactional
    public CartItem updateCartItem(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + cartItemId));
        if (quantity > 0) {
            cartItem.setQuantity(quantity);
            return cartItemRepository.save(cartItem);
        } else {
            cartItemRepository.delete(cartItem);
            return null;
        }
    }

    @Override
    @Transactional
    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    @Transactional
    public void mergeSessionCartWithDbCart(Map<Long, Integer> sessionCart, User user) {
        for (Map.Entry<Long, Integer> entry : sessionCart.entrySet()) {
            Long dishId = entry.getKey();
            Integer quantity = entry.getValue();

            Optional<Dish> dishOptional = dishService.findById(dishId);
            if (dishOptional.isPresent()) {
                addToCart(user, dishOptional.get(), quantity);
            }
        }
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        cartItemRepository.deleteByUser(user);
    }
}
