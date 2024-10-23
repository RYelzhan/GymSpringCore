package com.epam.wca.gym.dto.training;

import com.epam.wca.gym.aop.validation.ValidTrainer;
import com.epam.wca.gym.aop.validation.ValidTrainingType;
import com.epam.wca.gym.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;

public record TraineeTrainingQuery(
        @Size(min = 2, max = 50, message = "Trainer name must be between 2 and 25 characters")
        @ValidTrainer
        String trainerName,
        @JsonFormat(pattern = AppConstants.DEFAULT_DATE_FORMAT)
        ZonedDateTime dateFrom,
        @JsonFormat(pattern = AppConstants.DEFAULT_DATE_FORMAT)
        ZonedDateTime dateTo,
        @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 characters")
        @ValidTrainingType
        String trainingType
) {
}
