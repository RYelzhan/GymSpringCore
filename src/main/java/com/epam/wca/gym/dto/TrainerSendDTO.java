package com.epam.wca.gym.dto;

import java.util.Set;

public record TrainerSendDTO(
        String username,
        String firstName,
        String lastName,
        String trainingType,
        Boolean isActive,
        Set<TraineeBasicDTO> traineeAssigned
) {
}
