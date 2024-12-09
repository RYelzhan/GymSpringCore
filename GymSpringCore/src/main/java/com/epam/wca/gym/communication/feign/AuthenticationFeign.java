package com.epam.wca.gym.communication.feign;

import com.epam.wca.common.gymcommon.auth_dto.UserRegistrationDTO;
import com.epam.wca.gym.communication.feign.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "Authentication",
        configuration = {FeignConfig.class}
)
public interface AuthenticationFeign {
    @DeleteMapping(value = "/delete")
    void delete(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader);

    @PostMapping(value = "/register")
    String register(UserRegistrationDTO registrationDTO);
}
