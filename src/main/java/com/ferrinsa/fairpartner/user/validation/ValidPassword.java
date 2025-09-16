package com.ferrinsa.fairpartner.user.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordValidation.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface ValidPassword {
    String message() default "La contraseña no cumple con los requisitos de seguridad";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
