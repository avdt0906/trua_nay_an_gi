package com.codegym.project_module_5.service.impl;

import com.codegym.project_module_5.model.Orders;
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
    public Iterable<Orders> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Orders> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public void save(Orders order) {
        orderRepository.save(order);
    }

    @Override
    public long count() {
        return orderRepository.count();
    }
}
