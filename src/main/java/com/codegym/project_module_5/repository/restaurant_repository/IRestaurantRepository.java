package com.codegym.project_module_5.repository.restaurant_repository;

import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("SELECT r FROM Restaurant r WHERE r.user.username = :username")
    Optional<Restaurant> findByUsername(@Param("username") String username);
}
