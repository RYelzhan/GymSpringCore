package com.epam.wca.gym.aop.deprecated;

import com.epam.wca.gym.transaction.TransactionDetails;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Was used to check if transaction id works as planned
 */

@Deprecated(since = "2.0")

@Aspect
@Component
@RequiredArgsConstructor
public class TransactionAspect {
    @NonNull
    private TransactionDetails transactionDetails;

    @Pointcut("execution(* com.epam.wca.gym.controller..*.*(..))")
    public void transactionLoggingPointcut() {
        // This method is empty because it serves as a pointcut definition.
    }

    @Around(value = "transactionLoggingPointcut()")
    public Object logTransactionId(ProceedingJoinPoint pjp) throws Throwable {
        // Check if transactionId is not set, then set it
        if (transactionDetails.getId() == null) {
            transactionDetails.setId(generateTransactionId());
        }

        // Proceed with the method call
        return pjp.proceed();
    }

    private String generateTransactionId() {
        // You can generate the transaction ID here if needed
        return UUID.randomUUID().toString();
    }
}
