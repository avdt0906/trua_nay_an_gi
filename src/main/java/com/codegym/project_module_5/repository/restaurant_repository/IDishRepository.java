package com.codegym.project_module_5.repository.restaurant_repository;

import com.codegym.project_module_5.model.restaurant_model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface IDishRepository extends JpaRepository<Dish, Long> {
    Page<Dish> findAll(Pageable pageable);

    Iterable<Dish> findAllByRestaurantId(Long restaurantId);
    Iterable<Dish> findAllByRestaurantIdAndNameContainingIgnoreCase(Long restaurantId, String name);
    Iterable<Dish> findAllByRestaurantIsApprovedTrueAndRestaurantIsLockedFalse(); //Tìm tất cả các món ăn từ các nhà hàng đã được duyệt và đang hoạt động.
    Iterable<Dish> findAllByNameContainingIgnoreCaseAndRestaurantIsApprovedTrueAndRestaurantIsLockedFalse(String name); //Tìm kiếm các món ăn theo tên từ các nhà hàng đã được duyệt và đang hoạt động.
    Optional<Dish> findDishById(Long id);
    @Query("SELECT d FROM Dish d WHERE d.category.id = :categoryId AND d.id != :excludeDishId AND d.isAvailable = true ORDER BY d.id DESC LIMIT 6")
    List<Dish> findSimilarDishesByCategory(@Param("categoryId") Long categoryId, @Param("excludeDishId") Long excludeDishId);
    @Query("SELECT d FROM Dish d WHERE d.restaurant.id = :restaurantId AND d.id != :excludeDishId AND d.isAvailable = true ORDER BY d.id DESC LIMIT 6")
    List<Dish> findPopularDishesByRestaurant(@Param("restaurantId") Long restaurantId, @Param("excludeDishId") Long excludeDishId);

    List<Dish> findByRestaurant_Id(Long Id);


    List<Dish> findTop8ByOrderByDiscountDesc();
    
    @Query("SELECT d FROM Dish d WHERE d.category.id = :categoryId AND d.restaurant.isApproved = true AND d.restaurant.isLocked = false AND d.isAvailable = true")
    List<Dish> findByCategoryIdAndRestaurantApproved(@Param("categoryId") Long categoryId);
    
    @Query("SELECT d FROM Dish d WHERE d.category.id = :categoryId AND d.restaurant.isApproved = true AND d.restaurant.isLocked = false AND d.isAvailable = true")
    Page<Dish> findByCategoryIdAndRestaurantApproved(@Param("categoryId") Long categoryId, Pageable pageable);
    
    @Query("SELECT d FROM Dish d WHERE d.restaurant.isApproved = true AND d.restaurant.isLocked = false AND d.isAvailable = true AND d.discount > 0 ORDER BY d.discount DESC")
    List<Dish> findBestPriceDishes(Pageable pageable);
    
    @Query("SELECT d FROM Dish d WHERE d.restaurant.isApproved = true AND d.restaurant.isLocked = false AND d.isAvailable = true ORDER BY d.id DESC")
    List<Dish> findHotPickDishes(Pageable pageable);
    
    @Query("SELECT d FROM Dish d WHERE d.restaurant.isApproved = true AND d.restaurant.isLocked = false AND d.isAvailable = true ORDER BY RAND()")
    List<Dish> findNearbyDishes(Pageable pageable);
}
