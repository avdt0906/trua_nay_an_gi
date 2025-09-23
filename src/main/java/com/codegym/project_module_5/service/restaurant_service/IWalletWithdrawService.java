package com.codegym.project_module_5.service.restaurant_service;

import java.util.List;

import com.codegym.project_module_5.model.restaurant_model.WalletWithdraw;

public interface IWalletWithdrawService {
    WalletWithdraw createWithdrawRequest(Long restaurantId, Double amount, String note);
    List<WalletWithdraw> getRequestsByRestaurant(Long restaurantId);
    List<WalletWithdraw> getRequestsByStatus(WalletWithdraw.Status status);
    WalletWithdraw approveRequest(Long requestId, String adminUser);
    WalletWithdraw rejectRequest(Long requestId, String adminUser, String adminNote);
}
