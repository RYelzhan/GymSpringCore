package com.epam.wca.gym.communication.feign.config;

import brave.Tracing;
import com.epam.wca.gym.communication.feign.decoder.StatisticsErrorDecoder;
import com.epam.wca.gym.communication.feign.interceptor.TracingInterceptor;
import com.epam.wca.gym.communication.feign.interceptor.TransactionIdInterceptor;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FeignConfig {
    private final Tracing tracing;

    @Bean
    public RequestInterceptor transactionIdInterceptor() {
        return new TransactionIdInterceptor();
    }

    @Bean
    public RequestInterceptor tracingInterceptor() {
        return new TracingInterceptor(tracing);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new StatisticsErrorDecoder();
    }
}
