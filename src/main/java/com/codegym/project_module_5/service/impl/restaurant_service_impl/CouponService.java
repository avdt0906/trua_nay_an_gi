package com.codegym.project_module_5.service.impl.restaurant_service_impl;

import com.codegym.project_module_5.model.restaurant_model.Coupon;
import com.codegym.project_module_5.repository.restaurant_repository.ICouponRepository;
import com.codegym.project_module_5.service.restaurant_service.ICouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
public class CouponService implements ICouponService {
    @Autowired
    private ICouponRepository couponRepository;

    @Override
    public Iterable<Coupon> findAll() {
        return couponRepository.findAll();
    }

    @Override
    public Optional<Coupon> findById(Long id) {
        return couponRepository.findById(id);
    }

    @Override
    public void save(Coupon coupon) {
        couponRepository.save(coupon);
    }

    @Override
    public void delete(Long id) {
        couponRepository.deleteById(id);
    }

    @Override
    public Page<Coupon> findAll(Pageable pageable) {
        return couponRepository.findAll(pageable);
    }

    @Override
    public Page<Coupon> findAllByRestaurantId(Pageable pageable, Long restaurantId) {
        return couponRepository.findAllByRestaurantId(pageable, restaurantId);
    }
}
