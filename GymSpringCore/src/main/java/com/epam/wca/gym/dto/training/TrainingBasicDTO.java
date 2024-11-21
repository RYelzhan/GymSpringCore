package com.epam.wca.gym.dto.training;

import java.time.ZonedDateTime;

public record TrainingBasicDTO(
        String trainingName,
        ZonedDateTime trainingDate,
        String trainingType,
        int trainingDuration,
        String partnerName
) {
}
