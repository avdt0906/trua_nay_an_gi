package com.codegym.project_module_5.repository.order_repository;

import com.codegym.project_module_5.model.order_model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    Optional<OrderStatus> findByName(String name);
    List<OrderStatus> findAll();

}
