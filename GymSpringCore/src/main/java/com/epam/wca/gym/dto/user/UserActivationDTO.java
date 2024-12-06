package com.epam.wca.gym.dto.user;

import jakarta.validation.constraints.NotNull;

public record UserActivationDTO(
        @NotNull(message = "Is active is required")
        Boolean isActive
) {
}
