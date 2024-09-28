package com.epam.wca.gym.dto.user;

public record AuthenticatedUserDTO(
        String username,
        String password
) {
}
