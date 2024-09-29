package com.epam.wca.gym.dto.trainee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TraineeTrainersUpdateDTO(
        @NotBlank(message = "Username is required")
        @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
        String username,
        @NotEmpty(message = "Trainers list is required")
        List<@NotBlank(message = "Trainer username is required")
                @Size(min = 2, max = 50, message = "Trainer username must be between 2 and 50 characters")
                String> trainerUsernames
) {
}
