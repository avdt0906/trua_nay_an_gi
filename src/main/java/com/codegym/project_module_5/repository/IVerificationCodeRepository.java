package com.codegym.project_module_5.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codegym.project_module_5.model.VerificationCode;

public interface IVerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByEmailAndCode(String email, String code);
    void deleteByEmail(String email);
    Optional<VerificationCode> findByEmail(String email);

    
}

