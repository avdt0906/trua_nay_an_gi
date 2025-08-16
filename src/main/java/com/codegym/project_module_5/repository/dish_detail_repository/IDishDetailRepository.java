package com.codegym.project_module_5.repository.dish_detail_repository;

import com.codegym.project_module_5.model.restaurant_model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDishDetailRepository extends JpaRepository<Dish, Long> {
    Optional<Dish> findDishById(Long id);
    
    @Query("SELECT d FROM Dish d WHERE d.category.id = :categoryId AND d.id != :excludeDishId AND d.isAvailable = true ORDER BY d.id DESC LIMIT 6")
    List<Dish> findSimilarDishesByCategory(@Param("categoryId") Long categoryId, @Param("excludeDishId") Long excludeDishId);
    
    @Query("SELECT d FROM Dish d WHERE d.restaurant.id = :restaurantId AND d.id != :excludeDishId AND d.isAvailable = true ORDER BY d.id DESC LIMIT 6")
    List<Dish> findPopularDishesByRestaurant(@Param("restaurantId") Long restaurantId, @Param("excludeDishId") Long excludeDishId);
}
