package com.epam.wca.gym.dto;

import java.time.ZonedDateTime;

// TODO: Hibernate Validation
public record TraineeDTO(
        String firstName,
        String lastName,
        ZonedDateTime dateOfBirth,
        String address
) {
}
