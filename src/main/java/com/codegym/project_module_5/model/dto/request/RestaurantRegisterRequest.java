package com.codegym.project_module_5.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RestaurantRegisterRequest {
    
    @NotBlank(message = "Tên nhà hàng không được để trống")
    private String name;
    
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", 
             message = "Email không được chứa ký tự đặc biệt ngoại trừ _-.")
    private String email;
    
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
    
    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;
    private String address;
    private String phone;
    private String description;
}
