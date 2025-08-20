package com.codegym.project_module_5.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class UserProfileRequest {
    @NotBlank(message = "Username is required")
    private String fullName;
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Phone number is required")
    private String phone;
    private String avatarUrl;

    public UserProfileRequest() {
    }
}