package com.epam.wca.statistics.util;

import com.epam.wca.common.gymcommon.statistics_dto.Months;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerWorkloadSummary;
import com.epam.wca.statistics.entity.TrainerTrainingSummary;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class DTOFactory {
    public static TrainerWorkloadSummary convertToDto(
            TrainerTrainingSummary trainerTrainingSummary
    ) {
        return new TrainerWorkloadSummary(
                trainerTrainingSummary.getUsername(),
                convertToTrainingSummary(trainerTrainingSummary.getSummary())
        );
    }

    public static TrainerWorkloadSummary createEmptyDTO(String username) {
        return new TrainerWorkloadSummary(username, new HashMap<>());
    }

    private static Map<Integer, Map<Months, Integer>> convertToTrainingSummary(Map<Integer, Map<Integer, Integer>> summary) {
        if (summary == null) {
            return Collections.emptyMap();
        }

        return summary.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // Year as key
                        entry -> convertMonthMap(entry.getValue()) // Convert inner map
                ));
    }

    private static Map<Months, Integer> convertMonthMap(Map<Integer, Integer> monthMap) {
        if (monthMap == null) {
            return Collections.emptyMap();
        }

        return monthMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> Months.fromNumber(entry.getKey()), // Convert month number to Months enum
                        Map.Entry::getValue // Keep duration as value
                ));
    }
}
