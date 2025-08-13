package com.codegym.project_module_5.service;

import com.codegym.project_module_5.model.OrderDetail;

public interface IOrderDetailService extends IGeneralService<OrderDetail> {
    Iterable<OrderDetail> findAllByOrderId(Long orderId);
}
