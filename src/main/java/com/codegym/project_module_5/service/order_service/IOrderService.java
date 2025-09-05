package com.codegym.project_module_5.service.order_service;

import com.codegym.project_module_5.model.order_model.Orders;
import com.codegym.project_module_5.service.general_service.IGeneralService;

public interface IOrderService extends IGeneralService<Orders> {
    long count();
    Iterable<Orders> findAllByRestaurantId(Long restaurantId);
    boolean cancelOrder(Long orderId);
    Iterable<Orders> findByUserId(Long userId);
}
