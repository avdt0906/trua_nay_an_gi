package com.codegym.project_module_5.repository.order_repository;

import com.codegym.project_module_5.model.order_model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderRepository extends JpaRepository<Orders, Long> {
    Iterable<Orders> findAllByRestaurantId(Long restaurantId);
    Iterable<Orders> findByUser_IdOrderByIdDesc(Long userId);
}
