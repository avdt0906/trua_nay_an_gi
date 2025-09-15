package com.codegym.project_module_5.service.order_service;

import com.codegym.project_module_5.model.order_model.Orders;
import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.service.general_service.IGeneralService;

import java.util.List;

public interface IOrderService extends IGeneralService<Orders> {
    long count();
    Iterable<Orders> findAllByRestaurantId(Long restaurantId);
    boolean cancelOrder(Long orderId);
    List<Orders> findOrdersByUser(User user);
}
