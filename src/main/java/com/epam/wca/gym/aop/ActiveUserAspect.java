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

/**
 * @deprecated This class is deprecated. It was used in console version of application.
 */

@Deprecated(since = "2.0")

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ActiveUserAspect {
    @NonNull
    private final UserSession userSession;
    @NonNull
    private final Scanner scanner;

    /**
     * Pointcut for methods annotated with {@link ActiveUser}.
     *
     * <p>This method is intentionally left empty as it only serves as a
     * pointcut definition. It allows advice to be applied to any method
     * annotated with {@code @ActiveUser}.</p>
     */
    @Pointcut("@annotation(ActiveUser)")
    public void callAtActiveUserAnnotation() {
        // This method is empty because it serves as a pointcut definition.
    }

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
