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
        ElementType.RECORD_COMPONENT
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TraineeValidator.class)
public @interface ValidTrainee {
    String message() default "Trainee does not exist"; // default to an error message
    Class<?>[] groups() default {}; // for user to customize the targeted groups
    Class<? extends Payload>[] payload() default {}; // default {}; for extensibility purposes
}
