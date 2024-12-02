package com.epam.wca.statistics.config;

import com.epam.wca.common.gymcommon.aop.LoggingAspect;
import com.epam.wca.statistics.interceptor.LoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableAspectJAutoProxy
public class WebConfig implements WebMvcConfigurer {
    private final LoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
    }

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
