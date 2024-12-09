package com.epam.wca.gym.communication.feign.decoder;


import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ServiceUnavailableException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

public class StatisticsErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
/*        return ExceptionStrategy.defineStrategy(response.status())
                .map(ExceptionStrategy::getException)
                .orElse(errorDecoder.decode(methodKey, response));

 */
        return switch (response.status()) {
            case 400 -> new BadRequestException("Bad Request occurred");
            case 404 -> new NotFoundException("Resource not found");
            case 500 -> new InternalServerErrorException("Internal Server Error");
            case 503 -> new ServiceUnavailableException("Service is unavailable");
            default -> errorDecoder.decode(methodKey, response); // Use default error decoder for other cases
        };
    }

    // Functional Style
    @RequiredArgsConstructor
    @Getter
    enum ExceptionStrategy {
        BAD_REQUEST(400, new BadRequestException("Bad Request occurred")),
        RESOURCE_NOT_FOUND(404, new NotFoundException("Resource not found")),
        INTERNAL_SERVER(500, new InternalServerErrorException("Internal Server Error")),
        SERVER_UNAVAILABLE(503, new ServiceUnavailableException("Service is unavailable"));

        private final int code;
        private final Exception exception;

        public static Optional<ExceptionStrategy> defineStrategy(final int code) {
            return Arrays.stream(ExceptionStrategy.values())
                    .filter(strategy -> strategy.code == code)
                    .findFirst();
        }
    }
}
