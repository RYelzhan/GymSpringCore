package com.epam.wca.gym.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class RequestCounterMetrics {
    private static final String REQUEST_COUNTER_NAME = "request_counter";
    private final Counter counter;

    public RequestCounterMetrics(MeterRegistry meterRegistry) {
        this.counter = Counter.builder(REQUEST_COUNTER_NAME)
                .tag("type", "request")
                .description("Total number of Requests received.")
                .register(meterRegistry);
    }

    public void increaseCounter() {
        counter.increment();
    }
}
