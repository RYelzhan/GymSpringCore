package com.epam.wca.gym.aop.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.CONSTRUCTOR,
        ElementType.TYPE_USE,
        ElementType.RECORD_COMPONENT
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TrainerExistsValidator.class)
public @interface TrainerExists {
    String message() default "Trainer does not exist"; // default to an error message
    Class<?>[] groups() default {}; // for user to customize the targeted groups
    Class<? extends Payload>[] payload() default {}; // default {}; for extensibility purposes
}
