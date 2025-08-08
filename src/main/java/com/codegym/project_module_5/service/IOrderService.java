package com.codegym.project_module_5.service;

import com.codegym.project_module_5.model.Orders;

public interface IOrderService extends IGeneralService<Orders> {
    long count();
}
