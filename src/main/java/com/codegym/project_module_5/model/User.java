package com.codegym.project_module_5.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class  User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String fullName;
    private String avatar_url;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
