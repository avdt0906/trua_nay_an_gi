package com.codegym.project_module_5.repository.order_repository;

import com.codegym.project_module_5.model.order_model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderStatusRepository extends JpaRepository<OrderStatus, Long> {
}
