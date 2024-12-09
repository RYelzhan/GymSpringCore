package com.epam.wca.gym.aop.argument;

import com.epam.wca.common.gymcommon.exception.InternalErrorException;
import com.epam.wca.gym.repository.UserRepository;
import com.epam.wca.gym.transaction.UserDetailsContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(InsertUser.class);
    }

    @Override
    public Object resolveArgument(
            @NonNull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            @NonNull NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        return userRepository.findById(UserDetailsContext.getUserId())
                .orElseThrow(() ->
                        new InternalErrorException("Controller method reached with not existing user id" + parameter));
    }
}
