package com.epam.wca.gym.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @deprecated This class is deprecated. It was used in console version of application.
 */

@Deprecated(since = "2.0")

/**
 *  Indicates that that method can only be used by active users
 *
 *  <p> This method should be used on methods that require user
 *  to be active to interact with method.</p>
 *
 * @see com.epam.wca.gym.entity.User
 * @see com.epam.wca.gym.facade.user.UserSession
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActiveUser {
}
