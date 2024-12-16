package com.epam.wca.gym.communication.impl;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.auth_dto.UserAuthenticatedDTO;
import com.epam.wca.common.gymcommon.auth_dto.UserRegistrationDTO;
import com.epam.wca.common.gymcommon.exception.InternalErrorException;
import com.epam.wca.common.gymcommon.logging.TransactionContext;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;
import com.epam.wca.gym.communication.AuthenticationCommunicationService;
import com.epam.wca.gym.communication.feign.AuthenticationFeign;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationCommunicationServiceImpl implements AuthenticationCommunicationService {
    private final AuthenticationFeign authenticationFeign;

    @Override
    @Logging
    @CircuitBreaker(name = "statistics", fallbackMethod = "fallback")
    @Retry(name = "statistics")
    public UserAuthenticatedDTO userRegister(UserRegistrationDTO dto) {
        return authenticationFeign.register(dto);
    }

    public void fallback(TrainersTrainingsDeleteDTO trainingsDeleteDTO, Throwable throwable) {
        logErrorMessage(throwable);

        throw new InternalErrorException("Registration is impossible right now. Retry later.");
    }

    private static void logErrorMessage(Throwable e) {
        log.error("TransactionId: {} | Error of authenticating to Auth Service. Error: {}",
                TransactionContext.getTransactionId(), e.getMessage());
    }
}
