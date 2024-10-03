package com.epam.wca.gym.dto.training;

import com.epam.wca.gym.entity.TrainingType;

import java.time.ZonedDateTime;

public record TrainingDTO(
        long traineeId,
        long trainerId,
        String trainingName,
        TrainingType trainingType,
        ZonedDateTime trainingDate,
        int trainingDuration
) {
}
