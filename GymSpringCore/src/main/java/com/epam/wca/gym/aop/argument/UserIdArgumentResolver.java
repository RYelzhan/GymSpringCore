package com.epam.wca.gym.aop.argument;

import com.epam.wca.common.gymcommon.exception.InternalErrorException;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.repository.UserRepository;
import com.epam.wca.gym.transaction.UserDetailsContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @deprecated was used to Insert UserId into controller methods
 * Only usage was trainee/trainer deletion, which were refactored, hence no need for annotation anymore
 */
@Deprecated(since = "2.3")
//@Component
@RequiredArgsConstructor
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserRepository userRepository;

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
        User user = userRepository.findUserByUsername(UserDetailsContext.getUsername())
                .orElseThrow(
                        () ->
                        new InternalErrorException("Controller method reached with not existing username" + parameter));

        Long id = user.getId();

        userRepository.save(user);

        return id;
    }
}
