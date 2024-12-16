package com.epam.wca.gym.communication;

import com.epam.wca.common.gymcommon.auth_dto.UserRegistrationDTO;
import com.epam.wca.common.gymcommon.auth_dto.UserAuthenticatedDTO;

public interface AuthenticationCommunicationService {
    UserAuthenticatedDTO userRegister(UserRegistrationDTO userRegistrationDTO);
}
