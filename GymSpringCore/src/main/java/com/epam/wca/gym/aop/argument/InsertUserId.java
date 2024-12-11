package com.epam.wca.gym.aop.argument;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @deprecated was used to Insert UserId into controller methods
 * Only usage was trainee/trainer deletion, which were refactored, hence no need for annotation anymore
 */
@Deprecated(since = "2.3")
@Target({ ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface InsertUserId {
}
