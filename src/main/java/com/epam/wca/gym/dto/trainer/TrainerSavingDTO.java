package com.epam.wca.gym.dto.trainer;

import com.epam.wca.gym.entity.TrainingType;

public record TrainerSavingDTO(
        String firstName,
        String lastName,
        TrainingType trainingType
) {
}
