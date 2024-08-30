package com.epam.wca.gym.dto;

import com.epam.wca.gym.entity.TrainingType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerDTO extends UserDTO{
    private TrainingType trainingType;

    public TrainerDTO(String firstName, String lastName, TrainingType trainingType) {
        super(firstName, lastName);
        this.trainingType = trainingType;
    }
}
