package com.codegym.project_module_5.service.impl.order_service_impl;

import com.codegym.project_module_5.model.order_model.OrderDetail;
import com.codegym.project_module_5.repository.order_repository.IOrderDetailRepository;
import com.codegym.project_module_5.service.order_service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public void delete(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public Iterable<OrderDetail> findAllByOrderId(Long orderId) {
        return orderDetailRepository.findAllByOrderId(orderId);
    }

    @Override
    public List<OrderDetail> findByDishId(Long orderId) {
        return orderDetailRepository.findByDishId(orderId);
    }

    @Override
    public List<OrderDetail> findAllByOrder_User_Id(Long userId) {
        return orderDetailRepository.findAllByOrder_User_Id(userId);
    }

}
