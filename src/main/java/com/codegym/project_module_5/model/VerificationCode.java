package com.codegym.project_module_5.model;

import java.sql.Timestamp; 

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;

import lombok.Setter;

@Entity
@Table(name = "verification_code")
@Setter
@Getter
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String code;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "expires_at")
    private Timestamp expiresAt;

    public VerificationCode() {
    }

    public VerificationCode(Long id, String email, String code, Timestamp createdAt, Timestamp expiresAt) {
        this.id = id;
        this.email = email;
        this.code = code;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}

