package com.coffee.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OpenForBusinessValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenForBusiness {
    String message() default "Shop is currently closed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 