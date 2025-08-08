package com.codegym.project_module_5.service.impl;

import org.springframework.stereotype.Service;
import com.codegym.project_module_5.model.VerificationCode;
import com.codegym.project_module_5.repository.VerificationCodeRepository;
import java.sql.Timestamp;


@Service

public class OtpService {
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;

    public OtpService(VerificationCodeRepository verificationCodeRepository, EmailService emailService) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.emailService = emailService;
    }

    public void generateAndSendOtp(String email, String name) {
        String otpCode = String.valueOf((int)(Math.random() * 900000) + 100000); // 6 số
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp expiry = new Timestamp(System.currentTimeMillis() + 5 * 60 * 1000); // 5 phút

        verificationCodeRepository.deleteByEmail(email); // Xóa code cũ nếu có

        VerificationCode verificationCode = new VerificationCode(null, email, otpCode, now, expiry);
        verificationCodeRepository.save(verificationCode);

        emailService.sendOtpEmail(email, name, otpCode);
    }

    public boolean verifyOtp(String email, String code) {
        return verificationCodeRepository.findByEmailAndCode(email, code)
                .filter(vc -> vc.getExpiresAt().after(new Timestamp(System.currentTimeMillis())))
                .map(vc -> {
                    verificationCodeRepository.deleteByEmail(email); // Xác minh xong xóa
                    return true;
                })
                .orElse(false);
    }
}
