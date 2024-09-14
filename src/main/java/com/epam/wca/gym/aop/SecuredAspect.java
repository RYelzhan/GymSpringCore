package com.epam.wca.gym.aop;

import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.facade.user.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class SecuredAspect {
    private final UserSession userSession;

    @Autowired
    public SecuredAspect(UserSession userSession) {
        this.userSession = userSession;
    }

    @Pointcut("@annotation(SecuredAnnotation) && args(user)")
    public void callAtMyServiceSecurityAnnotationUser(User user) { }

    @Pointcut("@annotation(SecuredAnnotation) && args(id)")
    public void callAtMyServiceSecurityAnnotationId(long id) { }

    @Around(value = "callAtMyServiceSecurityAnnotationUser(user)", argNames = "pjp, user")
    public void aroundCallAt(ProceedingJoinPoint pjp, User user) throws Throwable {
        log.info("Aspect triggered for method: " + pjp.getSignature());
        if (userSession.getUser().equals(user)) {
            pjp.proceed();
        }
    }

    @Around(value = "callAtMyServiceSecurityAnnotationId(id)", argNames = "pjp,id")
    public User aroundCallAt(ProceedingJoinPoint pjp, long id) throws Throwable {
        log.info("Aspect triggered for method: " + pjp.getSignature());
        if (userSession.getUser().getId().equals(id)) {
            return (User) pjp.proceed();
        }

        return null;
    }
}
