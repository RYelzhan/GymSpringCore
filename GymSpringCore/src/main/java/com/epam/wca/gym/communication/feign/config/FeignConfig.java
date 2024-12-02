package com.epam.wca.gym.communication.feign.config;

import com.epam.wca.gym.communication.feign.interceptor.TransactionIdInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor transactionIdInterceptor() {
        return new TransactionIdInterceptor();
    }
}
