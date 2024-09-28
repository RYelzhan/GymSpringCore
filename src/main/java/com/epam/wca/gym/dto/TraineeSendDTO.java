package com.epam.wca.gym.dto;

import java.time.ZonedDateTime;
import java.util.Set;

public record TraineeSendDTO(
        String firstName,
        String lastName,
        ZonedDateTime dateOfBirth,
        String address,
        Boolean isActive,
        Set<EmbeddedTrainerDTO> trainersAssigned
) {
}
