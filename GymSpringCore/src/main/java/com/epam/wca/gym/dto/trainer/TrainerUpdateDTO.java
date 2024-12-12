package com.epam.wca.gym.dto.trainer;

import com.epam.wca.gym.aop.validation.ValidTrainingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TrainerUpdateDTO(
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 25, message = "First name must be between 2 and 25 characters")
        String firstName,
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 25, message = "Last name must be between 2 and 25 characters")
        String lastName,
        @ValidTrainingType
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 characters")
        String trainingType
) {
}
