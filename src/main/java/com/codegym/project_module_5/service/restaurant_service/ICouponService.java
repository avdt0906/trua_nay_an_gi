package com.codegym.project_module_5.service.restaurant_service;

import com.codegym.project_module_5.model.restaurant_model.Coupon;
import com.codegym.project_module_5.service.general_service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICouponService extends IGeneralService<Coupon> {
    Page<Coupon> findAll(Pageable pageable);
    Page<Coupon> findAllByRestaurantId(Pageable pageable, Long restaurantId);
}
