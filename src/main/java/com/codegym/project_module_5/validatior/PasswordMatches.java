package com.codegym.project_module_5.validatior;
import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
@Documented
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {
    String message() default "Mật khẩu và xác nhận mật khẩu không khớp";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
