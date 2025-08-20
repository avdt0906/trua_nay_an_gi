package com.codegym.project_module_5.service.cart_service;

import com.codegym.project_module_5.model.cart_model.CartItem;
import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.user_model.User;

import java.util.List;
import java.util.Map;

public interface ICartService {

    List<CartItem> getCartItems(User user);

    void addToCart(User user, Dish dish, int quantity);
    CartItem updateCartItem(Long cartItemId, int quantity);
    void removeCartItem(Long cartItemId);
    void mergeSessionCartWithDbCart(Map<Long, Integer> sessionCart, User user);
    void clearCart(User user);
}
