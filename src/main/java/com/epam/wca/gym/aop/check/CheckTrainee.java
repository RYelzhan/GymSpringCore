package com.epam.wca.gym.aop.check;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to restrict access to methods that are intended to be executed only by users
 * with a `Trainee` role. It is used in conjunction with an Aspect to check the role of the
 * currently authenticated user.
 *
 * <p>When a method annotated with {@code @CheckTrainee} is invoked, the aspect will ensure
 * that the authenticated user, extracted from the HTTP request, is an instance of {@code Trainee}.
 * If the user is not a Trainee, a {@code ForbiddenActionException} will be thrown.
 *
 * <p>This annotation should be placed on methods where access needs to be restricted based on
 * the user role being a Trainee.
 *
 * @see com.epam.wca.gym.aop.check.CheckTraineeAspect
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckTrainee {
}
