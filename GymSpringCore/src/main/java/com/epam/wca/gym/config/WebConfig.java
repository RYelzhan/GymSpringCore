package com.epam.wca.gym.config;

import com.epam.wca.common.gymcommon.aop.LoggingAspect;
import com.epam.wca.gym.aop.argument.UserArgumentResolver;
import com.epam.wca.gym.aop.argument.UserIdArgumentResolver;
import com.epam.wca.gym.interceptor.LoggingInterceptor;
import com.epam.wca.gym.interceptor.UserDetailsInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final UserArgumentResolver userArgumentResolver;
    private final UserIdArgumentResolver userIdArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor());
        registry.addInterceptor(new UserDetailsInterceptor());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
        resolvers.add(userIdArgumentResolver);
    }

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
