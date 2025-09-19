package com.codegym.project_module_5.repository.order_repository;

import com.codegym.project_module_5.model.order_model.OrderStatus;
import com.codegym.project_module_5.model.order_model.OrderDetail;
import com.codegym.project_module_5.model.order_model.Orders;
import com.codegym.project_module_5.model.user_model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByRestaurantId(Long restaurantId);
    List<Orders> findAllByUserOrderByCreatedAtDesc(User user);
}
