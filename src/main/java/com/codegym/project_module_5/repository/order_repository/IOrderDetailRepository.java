package com.codegym.project_module_5.repository.order_repository;

import com.codegym.project_module_5.model.order_model.OrderDetail;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    Iterable<OrderDetail> findAllByOrderId(Long orderId);

    List<OrderDetail> findByDishId(Long dishId);
}
