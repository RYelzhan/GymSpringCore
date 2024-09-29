package com.epam.wca.gym.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserActivationDTO(
        @NotBlank(message = "Username is required")
        @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
        String username,
        @NotNull(message = "Is active is required")
        Boolean isActive
) {
}
