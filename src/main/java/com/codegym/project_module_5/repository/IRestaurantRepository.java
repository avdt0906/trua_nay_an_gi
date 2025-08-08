package com.codegym.project_module_5.repository;

import com.codegym.project_module_5.model.Restaurant;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IRestaurantRepository extends CrudRepository<Restaurant, Long> {
    Optional<Restaurant> findByUser_Id(Long userId);

    Optional<Restaurant> findByUser_Username(String userUsername);

    @Query("SELECT r FROM Restaurant r WHERE r.isApproved IS NULL")
    List<Restaurant> findAllIsApprovedNull();

}
