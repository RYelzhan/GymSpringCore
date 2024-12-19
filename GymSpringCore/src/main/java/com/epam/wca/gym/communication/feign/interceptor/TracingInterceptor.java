package com.epam.wca.gym.communication.feign.interceptor;

import brave.Tracing;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TracingInterceptor implements RequestInterceptor {
    private final Tracing tracing;

    @Override
    public void apply(RequestTemplate template) {
        var context = tracing.currentTraceContext().get();
        String spanId = context.spanIdString();
        String traceId = context.traceIdString();

        System.out.printf("Added headers: %s %s%n", spanId, traceId);

        template.header("X-B3-SpanId", spanId);
        template.header("X-B3-TraceId", traceId);
    }
}
