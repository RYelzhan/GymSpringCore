package com.epam.wca.gym.dto.trainer;

public record TrainerBasicDTO(
        String username,
        String firstName,
        String lastName,
        String trainingType
) {
}
