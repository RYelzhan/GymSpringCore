package com.epam.wca.gym.aop;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.ForbiddenActionException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CheckTraineeAspect {
    @Pointcut("@annotation(CheckTrainee) && args(.., request)")
    public void checkTraineePointcut(HttpServletRequest request) {
        // This method is empty because it serves as a pointcut definition.
    }

    @Around(value = "checkTraineePointcut(request)")
    public Object checkTrainee(ProceedingJoinPoint pjp, HttpServletRequest request) throws Throwable {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (authenticatedUser instanceof Trainee) {
            return pjp.proceed();
        }
        throw new ForbiddenActionException("You are not Trainee.");
    }
}
