package com.codegym.project_module_5.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
    @NotNull
    private String address;
    private String phone;
    private String logoUrl;
    private String description;
    private Boolean isLongTermPartner = false;
    private Boolean isOpen = true;
    private Boolean isLocked = false;
    private Boolean isApproved;
}
