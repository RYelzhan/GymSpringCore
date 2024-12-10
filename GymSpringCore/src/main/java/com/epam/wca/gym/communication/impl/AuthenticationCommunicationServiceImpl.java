package com.epam.wca.gym.communication.impl;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.auth_dto.UserRegistrationDTO;
import com.epam.wca.gym.communication.AuthenticationCommunicationService;
import com.epam.wca.gym.communication.feign.AuthenticationFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationCommunicationServiceImpl implements AuthenticationCommunicationService {
    private final AuthenticationFeign authenticationFeign;
    private final JmsTemplate jmsTemplate;

    @Override
    @Logging
    public void delete() {
        var request =
                ((ServletRequestAttributes) RequestContextHolder
                        .getRequestAttributes())
                        .getRequest();

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        log.info("Auth Header Sent to Auth Service.");

        authenticationFeign.delete(authHeader);
    }

    @Override
    public String register(UserRegistrationDTO registrationDTO) {
        return null;
    }
}
