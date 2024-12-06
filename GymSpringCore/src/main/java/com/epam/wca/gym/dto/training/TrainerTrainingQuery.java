package com.epam.wca.gym.dto.training;

import com.epam.wca.gym.aop.validation.ValidTrainee;
import com.epam.wca.common.gymcommon.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;

public record TrainerTrainingQuery(
        @Size(min = 2, max = 50, message = "Trainer name must be between 2 and 25 characters")
        @ValidTrainee
        String traineeName,
        @JsonFormat(pattern = AppConstants.DEFAULT_DATE_FORMAT)
        ZonedDateTime dateFrom,
        @JsonFormat(pattern = AppConstants.DEFAULT_DATE_FORMAT)
        ZonedDateTime dateTo
) {
}
