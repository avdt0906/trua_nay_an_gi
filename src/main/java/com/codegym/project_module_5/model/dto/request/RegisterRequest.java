package com.codegym.project_module_5.model.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String fullName;
    private String avatar_url;
    private String confim_password;
}
