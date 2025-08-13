package com.codegym.project_module_5.service.order_service;

import com.codegym.project_module_5.model.order_model.OrderDetail;
import com.codegym.project_module_5.service.general_service.IGeneralService;

public interface IOrderDetailService extends IGeneralService<OrderDetail> {
    Iterable<OrderDetail> findAllByOrderId(Long orderId);
}
