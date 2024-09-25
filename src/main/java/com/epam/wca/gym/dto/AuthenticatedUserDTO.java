package com.epam.wca.gym.dto;

public record AuthenticatedUserDTO(
        String username,
        String password
) {
}
