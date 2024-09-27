package com.epam.wca.gym.aop;

import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.facade.user.UserSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @deprecated This class is deprecated. It was used in console version of application.
 */

@Deprecated(since = "2.0")

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SecuredAspect {
    @NonNull
    private final UserSession userSession;

    /**
     * Pointcut for methods annotated with {@link Secured} and accepting a {@link User} argument.
     *
     * <p>This method serves as a pointcut definition. It matches any method annotated
     * with {@code @Secured} that also has a {@link User} argument. The method body
     * is intentionally left empty because it is used only to define the pointcut.</p>
     *
     * @param user the user object passed to the method
     */
    @Pointcut("@annotation(Secured) && args(user)")
    public void callAtSecuredAnnotationUser(User user) {
        // This method is empty because it serves as a pointcut definition.
    }

    /**
     * Pointcut for methods annotated with {@link Secured} and accepting a {@code long} argument.
     *
     * <p>This method defines a pointcut for methods annotated with {@code @Secured}
     * that accept a {@code long} argument. The method is intentionally left empty
     * as it only defines the pointcut for the AOP framework.</p>
     *
     * @param id the long ID passed to the method
     */
    @Pointcut("@annotation(Secured) && args(id)")
    public void callAtSecuredAnnotationId(long id) {
        // This method is empty because it serves as a pointcut definition.
    }

    @Around(value = "callAtSecuredAnnotationUser(user)", argNames = "pjp, user")
    public void aroundCallAt(ProceedingJoinPoint pjp, User user) throws Throwable {
        log.info("Aspect triggered for method: " + pjp.getSignature());
        if (userSession.getUser().equals(user)) {
            pjp.proceed();
        }
    }

    @Around(value = "callAtSecuredAnnotationId(id)", argNames = "pjp,id")
    public User aroundCallAt(ProceedingJoinPoint pjp, long id) throws Throwable {
        log.info("Aspect triggered for method: " + pjp.getSignature());
        if (userSession.getUser().getId().equals(id)) {
            return (User) pjp.proceed();
        }

        return null;
    }
}
