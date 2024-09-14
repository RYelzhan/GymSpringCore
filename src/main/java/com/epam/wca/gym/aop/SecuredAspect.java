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

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SecuredAspect {
    @NonNull
    private final UserSession userSession;

    @Pointcut("@annotation(Secured) && args(user)")
    public void callAtSecuredAnnotationUser(User user) { }

    @Pointcut("@annotation(Secured) && args(id)")
    public void callAtSecuredAnnotationId(long id) { }

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
