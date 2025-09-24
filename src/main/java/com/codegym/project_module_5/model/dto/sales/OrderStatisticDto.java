package com.codegym.project_module_5.model.dto.sales;

import lombok.Data;

public interface OrderStatisticDto {
    Integer getYear();
    Integer getMonth();
    Integer getWeek();
    Integer getQuarter();
    Double getTotalSales();
    Double getTotalOrders();
}

