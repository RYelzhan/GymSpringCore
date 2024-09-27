package com.epam.wca.gym.aop;

import com.epam.wca.gym.exception.ValidationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ValidateAspect {
    @Pointcut(value = "@annotation(Validate) && args(.., bindingResult)", argNames = "bindingResult")
    public void callAtSecuredAnnotationUser(BindingResult bindingResult) {
        // This method is empty because it serves as a pointcut definition.
    }

    @Around(value = "callAtSecuredAnnotationUser(bindingResult)", argNames = "pjp,bindingResult")
    public Object validate(ProceedingJoinPoint pjp, BindingResult bindingResult) throws Throwable {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach((error) -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            throw new ValidationException(errors);
        }
        return pjp.proceed();
    }
}
