package com.epam.wca.gym.communication;

import com.epam.wca.common.gymcommon.auth_dto.UserRegistrationDTO;

public interface AuthenticationCommunicationService {
    void delete();
    String register(UserRegistrationDTO registrationDTO);
}
