package com.epam.wca.authentication.dto;

import jakarta.validation.constraints.NotNull;

public record UserActivationDTO(
        @NotNull(message = "Is active is required")
        Boolean isActive
) {
}
