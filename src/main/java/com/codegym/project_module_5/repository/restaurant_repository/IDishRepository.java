package com.codegym.project_module_5.repository.restaurant_repository;

import com.codegym.project_module_5.model.restaurant_model.Dish;
import org.springframework.data.repository.CrudRepository;


public interface IDishRepository extends CrudRepository<Dish, Long> {
    Iterable<Dish> findAllByRestaurantId(Long restaurantId);
    Iterable<Dish> findAllByRestaurantIdAndNameContainingIgnoreCase(Long restaurantId, String name);
    Iterable<Dish> findAllByRestaurantIsApprovedTrueAndRestaurantIsLockedFalse(); //Tìm tất cả các món ăn từ các nhà hàng đã được duyệt và đang hoạt động.
    Iterable<Dish> findAllByNameContainingIgnoreCaseAndRestaurantIsApprovedTrueAndRestaurantIsLockedFalse(String name); //Tìm kiếm các món ăn theo tên từ các nhà hàng đã được duyệt và đang hoạt động.
}
