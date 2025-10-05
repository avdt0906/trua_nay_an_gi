package com.codegym.project_module_5.model.restaurant_model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletWithdraw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    private Double withdrawAmount;
    private LocalDateTime withdrawTime = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Status status = Status.PENDING;
    private String note;
    private String adminNote;
    private LocalDateTime processedAt;

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }
}
