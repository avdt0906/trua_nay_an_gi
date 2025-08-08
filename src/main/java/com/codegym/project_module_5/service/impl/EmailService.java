package com.codegym.project_module_5.service.impl;

import com.codegym.project_module_5.service.IEmailService;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {
    
    @Override
    public void sendRestaurantRegistrationSuccess(String toEmail, String restaurantName) {
        System.out.println("=== EMAIL NOTIFICATION ===");
        System.out.println("To: " + toEmail);
        System.out.println("Subject: Đăng ký nhà hàng thành công");
        System.out.println("Content: Chúc mừng! Nhà hàng '" + restaurantName + "' của bạn đã được đăng ký thành công.");
        System.out.println("Nhà hàng sẽ được admin xem xét và phê duyệt trong thời gian sớm nhất.");
        System.out.println("==========================");
    }
}
