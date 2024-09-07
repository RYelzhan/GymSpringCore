package com.epam.wca.gym.dto;

import java.time.LocalDate;

public record TraineeDTO(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String address
) {
}
