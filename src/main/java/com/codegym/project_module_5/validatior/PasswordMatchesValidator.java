package com.codegym.project_module_5.validatior;

import com.codegym.project_module_5.model.dto.request.RegisterRequest;

import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements jakarta.validation.ConstraintValidator<PasswordMatches, RegisterRequest> {

     @Override
    public boolean isValid(RegisterRequest registerRequest, ConstraintValidatorContext context) {
        if (registerRequest == null || registerRequest.getPassword() == null || registerRequest.getConfirmPassword() == null) {
            return false;
        }

        boolean matched = registerRequest.getPassword().equals(registerRequest.getConfirmPassword());

        if (!matched) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("passwords do not match")
                   .addPropertyNode("confirmPassword")
                   .addConstraintViolation();
        }

        return matched;
    }
    
}
