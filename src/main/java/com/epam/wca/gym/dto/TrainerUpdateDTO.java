package com.epam.wca.gym.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TrainerUpdateDTO(
        @NotBlank(message = "Username is required")
        @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
        String username,
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 25, message = "First name must be between 2 and 25 characters")
        String firstName,
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 25, message = "Last name must be between 2 and 25 characters")
        String lastName,
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 characters")
        String trainingType,
        @NotNull(message = "Is active is required")
        Boolean isActive
) {
}
