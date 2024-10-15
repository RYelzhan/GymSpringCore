package com.epam.wca.gym.aop;

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

@Aspect
@Component
public class CheckTrainerAspect {
    @Value("${gym.api.request.attribute.user}")
    private String authenticatedUserRequestAttributeName;

    @Pointcut("@annotation(CheckTrainer) && args(.., request)")
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
