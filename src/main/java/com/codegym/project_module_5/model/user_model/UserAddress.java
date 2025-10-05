package com.codegym.project_module_5.model.user_model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    private String fullAddress;

    private Double latitude;

    private Double longitude;

    private boolean defaultAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
}
