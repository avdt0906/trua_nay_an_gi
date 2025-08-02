package com.codegym.project_module_5.model;

import jakarta.persistence.*;
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
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String address;
    private String phone;
    private String logo_url;
    private String description;
    private Boolean is_long_term_partner = false;
    private Boolean is_open = true;
    private Boolean is_locked = false;
    private Boolean is_approved = false;
}
