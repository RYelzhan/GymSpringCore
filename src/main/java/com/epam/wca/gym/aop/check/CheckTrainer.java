package com.epam.wca.gym.aop.check;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to restrict access to methods that are intended to be executed only by users
 * with a `Trainer` role. It is used in conjunction with an Aspect to check the role of the
 * currently authenticated user.
 *
 * <p>When a method annotated with {@code @CheckTrainer} is invoked, the aspect will ensure
 * that the authenticated user, extracted from the HTTP request, is an instance of {@code Trainer}.
 * If the user is not a Trainer, a {@code ForbiddenActionException} will be thrown.
 *
 * <p>This annotation should be placed on methods where access needs to be restricted based on
 * the user role being a Trainer.
 *
 * @see com.epam.wca.gym.aop.check.CheckTrainerAspect
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckTrainer {
}
