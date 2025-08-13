package com.codegym.project_module_5.model.dto.request;

import com.codegym.project_module_5.validatior.PasswordMatches;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatches
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Phone number must be between 10 characters")
    private String phone;
    private String fullName;
     private String password;
    private String confirmPassword;
}
