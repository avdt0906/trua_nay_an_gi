package com.codegym.project_module_5.repository;

import com.codegym.project_module_5.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDishRepository extends JpaRepository<Dish, Long> {
    Iterable<Dish> findAllByRestaurantId(Long restaurantId);
    Iterable<Dish> findAllByRestaurantIdAndNameContainingIgnoreCase(Long restaurantId, String name);
    Iterable<Dish> findAllByRestaurantIsApprovedTrueAndRestaurantIsLockedFalse(); //Tìm tất cả các món ăn từ các nhà hàng đã được duyệt và đang hoạt động.
    Iterable<Dish> findAllByNameContainingIgnoreCaseAndRestaurantIsApprovedTrueAndRestaurantIsLockedFalse(String name); //Tìm kiếm các món ăn theo tên từ các nhà hàng đã được duyệt và đang hoạt động.
}
