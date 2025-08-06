package com.codegym.project_module_5.service;

import com.codegym.project_module_5.model.Order;

public interface IOrderService extends IGeneralService<Order> {
    long count();
}
