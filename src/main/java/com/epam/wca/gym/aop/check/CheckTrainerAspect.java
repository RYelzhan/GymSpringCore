package com.epam.wca.gym.aop.check;

import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.ForbiddenActionException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Aspect for enforcing access control based on the `Trainer` role.
 *
 * <p>This aspect intercepts method calls annotated with {@link CheckTrainer} and ensures that
 * the authenticated user is a Trainer. The authenticated user is retrieved from the
 * {@link HttpServletRequest} based on the attribute specified in the application's configuration.
 *
 * <p>If the user is not an instance of {@code Trainer}, a {@link ForbiddenActionException} is thrown,
 * indicating that the user is not authorized to perform the action.
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * @CheckTrainer
 * public void someMethod(HttpServletRequest request) {
 *     // method implementation
 * }
 * }
 * </pre>
 *
 * @see com.epam.wca.gym.aop.check.CheckTrainer
 */

@Aspect
@Component
public class CheckTrainerAspect {
    @Value("${gym.api.request.attribute.user}")
    private String authenticatedUserRequestAttributeName;

    @Pointcut("@annotation(com.epam.wca.gym.aop.check.CheckTrainer) && args(.., request)")
    public void checkTrainerPointcut(HttpServletRequest request) {
        // This method is empty because it serves as a pointcut definition.
    }

    @Around(value = "checkTrainerPointcut(request)", argNames = "pjp,request")
    public Object checkTrainer(ProceedingJoinPoint pjp, HttpServletRequest request) throws Throwable {
        User authenticatedUser = (User) request.getAttribute(authenticatedUserRequestAttributeName);

        if (authenticatedUser instanceof Trainer) {
            return pjp.proceed();
        }
        throw new ForbiddenActionException("You are not Trainer.");
    }
}
