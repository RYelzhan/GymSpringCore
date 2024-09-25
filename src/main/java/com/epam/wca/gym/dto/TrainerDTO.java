package com.epam.wca.gym.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record TrainerDTO(
        @NotEmpty(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        String firstName,
        @NotEmpty(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        String lastName,
        @NotEmpty(message = "Last name is required")
        @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 characters")
        String trainingType
) {
}
