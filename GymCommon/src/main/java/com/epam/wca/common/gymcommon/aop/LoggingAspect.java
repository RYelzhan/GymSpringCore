package com.epam.wca.common.gymcommon.aop;

import com.epam.wca.common.gymcommon.logging.TransactionContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class LoggingAspect {

    @Pointcut(value = "@annotation(com.epam.wca.common.gymcommon.aop.Logging)")
    public void loggingPointcut() {
        // This method is empty because it serves as a pointcut definition.
    }

    @Around(value = "loggingPointcut()")
    public Object logMethodDetails(ProceedingJoinPoint pjp) throws Throwable {
        String transactionId = TransactionContext.getTransactionId();

/*        if (transactionId == null) {
            throw new InternalErrorException("Method reached without creation of Transaction Id.");
        }

 */

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
}
