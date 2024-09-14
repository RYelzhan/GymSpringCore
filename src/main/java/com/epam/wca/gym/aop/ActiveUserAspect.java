package com.epam.wca.gym.aop;

import com.epam.wca.gym.facade.user.UserSession;
import com.epam.wca.gym.util.InputHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ActiveUserAspect {
    @NonNull
    private final UserSession userSession;
    @NonNull
    private final Scanner scanner;

    @Pointcut("@annotation(ActiveUser)")
    public void callAtActiveUserAnnotation() { }

    @Around(value = "callAtActiveUserAnnotation()", argNames = "pjp")
    public void aroundCallAt(ProceedingJoinPoint pjp) throws Throwable {
        if (userSession.getUser().isActive()) {
            pjp.proceed();
        } else {
            log.info("First you need to activate account: ");
            userSession.getUser().setActive(
                    InputHandler.promptForBoolean(scanner)
            );
        }
    }
}
