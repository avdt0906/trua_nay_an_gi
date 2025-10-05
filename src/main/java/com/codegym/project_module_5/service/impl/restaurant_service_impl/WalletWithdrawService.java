package com.codegym.project_module_5.service.impl.restaurant_service_impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.model.restaurant_model.WalletWithdraw;
import com.codegym.project_module_5.model.restaurant_model.WalletWithdraw.Status;
import com.codegym.project_module_5.repository.restaurant_repository.IRestaurantRepository;
import com.codegym.project_module_5.repository.restaurant_repository.WalletWithdrawRepository;
import com.codegym.project_module_5.service.restaurant_service.IWalletWithdrawService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class WalletWithdrawService implements IWalletWithdrawService {

    @Autowired
    WalletWithdrawRepository walletWithdrawRepository;
    @Autowired
    IRestaurantRepository restaurantRepository;
    

    @Override
    public WalletWithdraw createWithdrawRequest(Long restaurantId, Double amount, String note) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Số tiền rút phải lớn hơn 0");
        }
        if (restaurant.getWallet() < amount) {
            throw new IllegalArgumentException("Số dư không đủ để rút");
        }

        // KHÔNG trừ tiền ngay, chờ duyệt
        WalletWithdraw request = new WalletWithdraw();
        request.setRestaurant(restaurant);
        request.setWithdrawAmount(amount);
        request.setStatus(WalletWithdraw.Status.PENDING);
        request.setNote(note);
        return walletWithdrawRepository.save(request);
    }

    @Override
   public List<WalletWithdraw> getRequestsByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));
        return walletWithdrawRepository.findByRestaurantOrderByWithdrawTimeDesc(restaurant);
    }

    @Override
    public List<WalletWithdraw> getRequestsByStatus(Status status) {
        return walletWithdrawRepository.findByStatusOrderByWithdrawTimeAsc(status);
    }

    @Override
    public WalletWithdraw approveRequest(Long requestId, String adminUser) {
        WalletWithdraw req = walletWithdrawRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Withdraw request not found"));

        if (req.getStatus() != WalletWithdraw.Status.PENDING) {
            throw new IllegalStateException("Yêu cầu đã được xử lý");
        }

        Restaurant restaurant = req.getRestaurant();
        if (restaurant.getWallet() < req.getWithdrawAmount()) {
            throw new IllegalStateException("Số dư không đủ để duyệt");
        }

        // trừ tiền & cập nhật trạng thái
        restaurant.setWallet(restaurant.getWallet() - req.getWithdrawAmount());
        req.setStatus(WalletWithdraw.Status.APPROVED);
        req.setProcessedAt(LocalDateTime.now());
        req.setAdminNote("Yêu cầu đã được duyệt liên hệ chúng tôi nếu có vấn đề ! xin cảm ơn" );

        restaurantRepository.save(restaurant);
        return walletWithdrawRepository.save(req);
    }

    @Override
    public WalletWithdraw rejectRequest(Long requestId, String adminUser, String adminNote) {
        WalletWithdraw req = walletWithdrawRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Withdraw request not found"));

        if (req.getStatus() != WalletWithdraw.Status.PENDING) {
            throw new IllegalStateException("Yêu cầu đã được xử lý");
        }

        req.setStatus(WalletWithdraw.Status.REJECTED);
        req.setAdminNote(adminNote != null && !adminNote.isBlank()
                ? adminNote
                : "Từ chối bởi " + adminUser);
        req.setProcessedAt(LocalDateTime.now());
        return walletWithdrawRepository.save(req);
    }
    
}
