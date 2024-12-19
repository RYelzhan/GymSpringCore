package com.epam.wca.statistics.util;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerWorkloadSummary;
import com.epam.wca.statistics.entity.TrainerTrainingSummary;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class DTOFactory {
    @Logging
    public static TrainerWorkloadSummary convertToDto(
            String username,
            Map<Integer, Map<Integer,Integer>> trainerWorkloads
    ) {
        return new TrainerWorkloadSummary(username, trainerWorkloads);
    }

    public static TrainerWorkloadSummary convertToDto(
            TrainerTrainingSummary trainerTrainingSummary
    ) {
        return new TrainerWorkloadSummary(trainerTrainingSummary.getUsername(), trainerTrainingSummary.getSummary());
    }

    public static TrainerWorkloadSummary createEmptyDTO(String username) {
        return new TrainerWorkloadSummary(username, new HashMap<>());
    }
}
