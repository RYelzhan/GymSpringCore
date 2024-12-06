package com.epam.wca.gym.dto.user;

public record UserAuthenticatedDTO(
        String username,
        String password
) {
}
