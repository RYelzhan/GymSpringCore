package com.epam.wca.gym.dto;

import java.time.ZonedDateTime;

public record TraineeDTO(
        String firstName,
        String lastName,
        ZonedDateTime dateOfBirth,
        String address
) {
}
