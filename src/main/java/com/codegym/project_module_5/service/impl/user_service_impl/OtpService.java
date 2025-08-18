package com.codegym.project_module_5.service.impl.user_service_impl;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.codegym.project_module_5.model.user_model.VerificationCode;
import com.codegym.project_module_5.repository.user_repository.IVerificationCodeRepository;

import jakarta.transaction.Transactional;

@Service
public class OtpService {
    private final IVerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;

    public OtpService(IVerificationCodeRepository verificationCodeRepository, EmailService emailService) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void generateAndSendOtp(String email, String name) {
        String otpCode = String.valueOf((int) (Math.random() * 900000) + 100000);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp expiry = new Timestamp(System.currentTimeMillis() + 5 * 60 * 1000);

        VerificationCode verificationCode = verificationCodeRepository
                .findByEmail(email)
                .orElse(new VerificationCode());

        verificationCode.setEmail(email);
        verificationCode.setCode(otpCode);
        verificationCode.setCreatedAt(now);
        verificationCode.setExpiresAt(expiry);

        verificationCodeRepository.save(verificationCode);
        emailService.sendOtpEmail(email, name, otpCode);
    }

    @Transactional
    public boolean verifyOtp(String email, String code) {
        System.out.println("Verify request - Email: " + email + " | Code: " + code);
        return verificationCodeRepository.findByEmailAndCode(email.trim(), code.trim())
                .map(vc -> {
                    System.out.println("Found OTP in DB: " + vc);
                    if (vc.getExpiresAt().before(new Timestamp(System.currentTimeMillis()))) {
                        System.out.println("OTP expired");
                        verificationCodeRepository.deleteByEmail(email.trim());
                        return false;
                    }
                    verificationCodeRepository.deleteByEmail(email.trim());
                    return true;
                })
                .orElseGet(() -> {
                    System.out.println("Không tìm thấy OTP trong DB");
                    return false;
                });
    }

}
