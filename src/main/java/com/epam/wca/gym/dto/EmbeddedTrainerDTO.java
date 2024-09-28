package com.epam.wca.gym.dto;

public record EmbeddedTrainerDTO(
        String username,
        String firstName,
        String lastName,
        String trainingType
) {
}
