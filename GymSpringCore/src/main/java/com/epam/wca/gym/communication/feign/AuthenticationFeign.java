package com.epam.wca.gym.communication.feign;

import com.epam.wca.common.gymcommon.auth_dto.UserAuthenticatedDTO;
import com.epam.wca.common.gymcommon.auth_dto.UserRegistrationDTO;
import com.epam.wca.gym.communication.feign.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "Authentication",
        configuration = {FeignConfig.class}
)
public interface AuthenticationFeign {

    @PostMapping(value = "/register")
    UserAuthenticatedDTO register(UserRegistrationDTO registrationDTO);
}
