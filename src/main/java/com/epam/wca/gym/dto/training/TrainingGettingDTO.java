package com.epam.wca.gym.dto.training;

import com.epam.wca.gym.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;

public record TrainingGettingDTO(
        @NotBlank(message = "Trainee username is required")
        @Size(min = 2, max = 25, message = "Trainee username must be between 2 and 50 characters")
        String traineeUsername,
        @NotBlank(message = "Trainer username is required")
        @Size(min = 2, max = 25, message = "Trainer username must be between 2 and 50 characters")
        String trainerUsername,
        @NotBlank(message = "Training name is required")
        @Size(min = 2, max = 25, message = "Training name must be between 2 and 50 characters")
        String trainingName,
        @NotNull(message = "Training date is required")
        @Future(message = "Date of training must be in the future")
        @JsonFormat(pattern = AppConstants.DEFAULT_DATE_FORMAT)
        ZonedDateTime date,
        @NotNull(message = "Training duration is required")
        @Min(value = 10, message = "Minimum duration is 10 minutes")
        @Max(value = 360, message = "Max duration is 6 hours")
        Integer trainingDuration
) {
}
