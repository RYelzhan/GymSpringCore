package com.epam.wca.gym.communication.feign.decoder;


import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ServiceUnavailableException;

public class StatisticsErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new BadRequestException("Bad Request occurred");
            case 404 -> new NotFoundException("Resource not found");
            case 500 -> new InternalServerErrorException("Internal Server Error");
            case 503 -> new ServiceUnavailableException("Service is unavailable");
            default -> errorDecoder.decode(methodKey, response);
        };
    }
}
