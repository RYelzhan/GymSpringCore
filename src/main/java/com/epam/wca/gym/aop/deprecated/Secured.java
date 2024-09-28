package com.epam.wca.gym.aop.deprecated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @deprecated This class is deprecated. It was used in console version of application.
 */

@Deprecated(since = "2.0")

/**
 * Intended to check before any update action on entities
 * whether User requesting change have privileges to do so.
 *
 * <p> Should be used on methods to check if User has privileges
 * to change the entity </p>
 *
 * @see com.epam.wca.gym.entity.User
 * @see com.epam.wca.gym.facade.user.UserSession
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Secured {

}
