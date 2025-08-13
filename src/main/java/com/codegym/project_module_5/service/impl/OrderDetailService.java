package com.codegym.project_module_5.service.impl;

import com.codegym.project_module_5.model.OrderDetail;
import com.codegym.project_module_5.repository.IOrderDetailRepository;
import com.codegym.project_module_5.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderDetailService implements IOrderDetailService {

    @Autowired
    private IOrderDetailRepository orderDetailRepository;

    @Override
    public Iterable<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    @Override
    public Optional<OrderDetail> findById(Long id) {
        return orderDetailRepository.findById(id);
    }

    @Override
    public void save(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
    }

    @Override
    public Iterable<OrderDetail> findAllByOrderId(Long orderId) {
        return orderDetailRepository.findAllByOrderId(orderId);
    }
}
