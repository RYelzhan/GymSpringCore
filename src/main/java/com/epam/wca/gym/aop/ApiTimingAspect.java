package com.epam.wca.gym.aop;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Aspect
@Component
public class ApiTimingAspect {
    private final MeterRegistry meterRegistry;

    public ApiTimingAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Pointcut("execution(* com.epam.wca.gym.controller..*(..))")
    public void apiTimingPointcut() {
        // This method is empty because it serves as a pointcut definition.
    }

    @Around(value = "apiTimingPointcut()")
    public Object apiTiming(ProceedingJoinPoint pjp) {
        String className = pjp.getTarget().getClass().getSimpleName();
        String methodName = pjp.getSignature().getName();

        Timer timer = Timer.builder("api.response.time")
                .tag("class", className)
                .tag("method", methodName)
                .description("Average Api Response Time")
                .register(meterRegistry);

        long start = System.nanoTime();

        try {
            return pjp.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            long end = System.nanoTime();
            timer.record(Duration.ofNanos(end - start));
        }
    }
}
