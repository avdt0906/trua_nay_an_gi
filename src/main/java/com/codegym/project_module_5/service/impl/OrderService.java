package com.codegym.project_module_5.service.impl;

import com.codegym.project_module_5.model.Order;
import com.codegym.project_module_5.repository.IOrderRepository;
import com.codegym.project_module_5.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public Iterable<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public long count() {
        return orderRepository.count();
    }
}
