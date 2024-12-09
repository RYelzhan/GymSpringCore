package com.epam.wca.gym.aop.argument;

import com.epam.wca.common.gymcommon.exception.InternalErrorException;
import com.epam.wca.gym.transaction.UserDetailsContext;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(InsertUserId.class);
    }

    @Override
    public Object resolveArgument(
            @NonNull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            @NonNull NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        if (UserDetailsContext.getUserId() == null) {
            throw new InternalErrorException("Controller method reached with not existing user id" + parameter);
        }

        return UserDetailsContext.getUserId();
    }
}
