package com.epam.wca.statistics.util;

import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.statistics.entity.TrainerTrainingSummary;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class EntityFactory {
    public static TrainerTrainingSummary covertToDocument(TrainerTrainingAddDTO trainingAddDTO) {
        Map<Integer, Integer> monthSummary = new HashMap<>();
        monthSummary.put(trainingAddDTO.date().getMonthValue(), trainingAddDTO.duration());

        Map<Integer, Map<Integer, Integer>> yearSummary = new HashMap<>();
        yearSummary.put(trainingAddDTO.date().getYear(), monthSummary);

        return new TrainerTrainingSummary(
                trainingAddDTO.username(),
                trainingAddDTO.firstname(),
                trainingAddDTO.lastname(),
                trainingAddDTO.isActive(),
                yearSummary
        );
    }
}
