package com.epam.wca.gym.dto.trainee;

import com.epam.wca.gym.aop.validation.ValidTrainer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TraineeTrainersUpdateDTO(
        @NotEmpty(message = "Trainers list is required")
        List<@NotBlank(message = "Trainer username is required")
                @Size(min = 2, max = 50, message = "Trainer username must be between 2 and 50 characters")
                @ValidTrainer
                String> trainerUsernames
) {
}
