package com.epam.wca.statistics.util;

import com.epam.wca.statistics.dto.TrainerTrainingAddDTO;
import com.epam.wca.statistics.entity.TrainerWorkload;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EntityFactory {
    public static TrainerWorkload covertToZeroDurationEntity(TrainerTrainingAddDTO trainingAddDTO) {
        return new TrainerWorkload(
                trainingAddDTO.username(),
                trainingAddDTO.firstname(),
                trainingAddDTO.lastname(),
                trainingAddDTO.isActive(),
                trainingAddDTO.date().getYear(),
                trainingAddDTO.date().getMonthValue(),
                0
        );
    }
}
