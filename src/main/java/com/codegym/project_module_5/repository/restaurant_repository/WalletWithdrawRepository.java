package com.codegym.project_module_5.repository.restaurant_repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.model.restaurant_model.WalletWithdraw;

public interface WalletWithdrawRepository extends JpaRepository<WalletWithdraw, Long> {
    List<WalletWithdraw> findByRestaurantOrderByWithdrawTimeDesc(Restaurant restaurant);
    List<WalletWithdraw> findByStatusOrderByWithdrawTimeAsc(WalletWithdraw.Status status);
}