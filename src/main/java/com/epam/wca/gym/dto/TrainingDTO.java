package com.epam.wca.gym.dto;

import com.epam.wca.gym.entity.TrainingType;

import java.time.LocalDate;

public record TrainingDTO(
        long traineeId,
        long trainerId,
        String trainingName,
        TrainingType trainingType,
        LocalDate trainingDate,
        int trainingDuration
) {
}
