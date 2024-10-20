package com.epam.wca.gym.aop.check;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.ForbiddenActionException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CheckTraineeAspect {
    @Value("${gym.api.request.attribute.user}")
    private String authenticatedUserRequestAttributeName;

    @Pointcut("@annotation(com.epam.wca.gym.aop.check.CheckTrainee) && args(.., request)")
    public void checkTraineePointcut(HttpServletRequest request) {
        // This method is empty because it serves as a pointcut definition.
    }

    @Around(value = "checkTraineePointcut(request)", argNames = "pjp,request")
    public Object checkTrainee(ProceedingJoinPoint pjp, HttpServletRequest request) throws Throwable {
        User authenticatedUser = (User) request.getAttribute(authenticatedUserRequestAttributeName);

        System.out.println(authenticatedUser);

        if (authenticatedUser instanceof Trainee) {
            return pjp.proceed();
        }
        throw new ForbiddenActionException("You are not Trainee.");
    }
}
