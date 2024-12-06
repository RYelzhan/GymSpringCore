package com.epam.wca.gym.dto.trainer;

import com.epam.wca.gym.dto.trainee.TraineeBasicDTO;

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
