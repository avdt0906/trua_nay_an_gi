package com.codegym.project_module_5.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Shipper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @UniqueElements
    private String name;
    @NotNull
    private String phone;
    @NotNull
    private Double price;
    @NotNull
    private Integer deliverMinute;
    private Boolean isAvailable = true;
    private Boolean isLocked = false;
    private String avatarUrl;
}
