package com.codegym.project_module_5.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserAddressRequest {

    @NotNull
    private String name; 

    @NotNull
    private String phone; 

    @NotNull
    private String fullAddress; 
    private boolean defaultAddress;
    private Double latitude;
    private Double longitude;

  
}