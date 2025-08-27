package com.codegym.project_module_5.repository.cart_repository;

import com.codegym.project_module_5.model.cart_model.CartItem;
import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.user_model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ICartDetailRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndDish(User user, Dish dish);
    @Query("SELECT ci FROM CartItem ci WHERE ci.user.id = :userId AND ci.dish.id = :dishId")
    Optional<CartItem> findByUserIdAndDishId(@Param("userId") Long userId, @Param("dishId") Long dishId);
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);
    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.user.id = :userId")
    Optional<Integer> getTotalQuantityByUserId(@Param("userId") Long userId);
    @Query("SELECT SUM(ci.dish.price * ci.quantity) FROM CartItem ci WHERE ci.user.id = :userId")
    Optional<Double> getTotalPriceByUserId(@Param("userId") Long userId);
    @Query("SELECT ci FROM CartItem ci WHERE ci.user.id = :userId")
    List<CartItem> findByUserId(@Param("userId") Long userId);
    void deleteByUser(User user);
    @Query("DELETE FROM CartItem ci WHERE ci.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
    @Query("SELECT CASE WHEN COUNT(ci) > 0 THEN true ELSE false END FROM CartItem ci WHERE ci.user.id = :userId AND ci.dish.id = :dishId")
    boolean existsByUserIdAndDishId(@Param("userId") Long userId, @Param("dishId") Long dishId);
    @Query("SELECT ci FROM CartItem ci WHERE ci.user.id = :userId AND ci.dish.category.id = :categoryId")
    List<CartItem> findByUserIdAndCategoryId(@Param("userId") Long userId, @Param("categoryId") Long categoryId);
    @Query("SELECT ci FROM CartItem ci WHERE ci.user.id = :userId AND ci.dish.restaurant.id = :restaurantId")
    List<CartItem> findByUserIdAndRestaurantId(@Param("userId") Long userId, @Param("restaurantId") Long restaurantId);
}
