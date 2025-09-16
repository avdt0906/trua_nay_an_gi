package com.codegym.project_module_5.repository.order_repository;

import com.codegym.project_module_5.model.order_model.OrderDetail;
import com.codegym.project_module_5.model.order_model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Orders, Long> {
    Iterable<Orders> findAllByRestaurantId(Long restaurantId);
//    List<Orders> findAllByOrderId(Long orderId, Long userId);
}
