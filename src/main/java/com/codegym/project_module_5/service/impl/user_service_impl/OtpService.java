package com.codegym.project_module_5.service.impl.user_service_impl;

import java.sql.Timestamp;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.codegym.project_module_5.common.OtpStatus;
import com.codegym.project_module_5.model.user_model.VerificationCode;
import com.codegym.project_module_5.repository.user_repository.IVerificationCodeRepository;

import jakarta.transaction.Transactional;

@Service
public class OtpService {
    private static final Logger log = LoggerFactory.getLogger(OtpService.class);
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
    public OtpStatus verifyOtp(String email, String code) {
        String normalizedEmail = email.trim().toLowerCase();
        String normalizedCode = code.trim();

        log.info("Verify OTP: email={}, code={}", normalizedEmail, normalizedCode);

        return verificationCodeRepository.findByEmailAndCode(normalizedEmail, normalizedCode)
                .map(vc -> {
                    if (vc.getExpiresAt().toInstant().isBefore(Instant.now())) {
                        log.warn("OTP expired for email={}", normalizedEmail);
                        verificationCodeRepository.deleteByEmail(normalizedEmail);
                        return OtpStatus.EXPIRED;
                    }
                    verificationCodeRepository.deleteByEmail(normalizedEmail);
                    log.info("OTP valid for email={}, deleted from DB", normalizedEmail);
                    return OtpStatus.VALID;
                })
                .orElseGet(() -> {
                    log.warn("OTP not found for email={}", normalizedEmail);
                    return OtpStatus.INVALID;
                });
    }
}
