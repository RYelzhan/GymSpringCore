package com.epam.wca.gym.dto.trainee;

import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;

import java.time.ZonedDateTime;
import java.util.Set;

public record TraineeSendDTO(
        String firstName,
        String lastName,
        ZonedDateTime dateOfBirth,
        String address,
        Boolean isActive,
        Set<TrainerBasicDTO> trainersAssigned
) {
}
