package com.epam.wca.gym.aop;

import com.epam.wca.gym.transaction.TransactionDetails;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    @NonNull
    private TransactionDetails transactionDetails;

    @Pointcut(value = "@annotation(com.epam.wca.gym.aop.Logging)")
    public void loggingPointcut() {
        // This method is empty because it serves as a pointcut definition.
    }

    @Around(value = "loggingPointcut()")
    public Object logMethodDetails(ProceedingJoinPoint pjp) throws Throwable {
        if (transactionDetails.getId() == null) {
            transactionDetails.setId(generateTransactionId());
        }

        String transactionId = transactionDetails.getId();
        String methodName = pjp.getSignature().toShortString();
        Object[] methodArgs = pjp.getArgs();

        log.info("Transaction ID: {} | Invoking method: {} with args: {}", transactionId, methodName, methodArgs);

        Object result;
        try {
            result = pjp.proceed();  // Proceed with the method call
        } catch (Throwable throwable) {
            log.error("Transaction ID: {} | Exception in method: {} with args: {} | Exception: {}",
                    transactionId, methodName, methodArgs, throwable.getMessage());
            throw throwable;
        }

        log.info("Transaction ID: {} | Method: {} returned: {}", transactionId, methodName, result);
        return result;
    }

    private String generateTransactionId() {
        // You can generate the transaction ID here if needed
        return UUID.randomUUID().toString();
    }
}
