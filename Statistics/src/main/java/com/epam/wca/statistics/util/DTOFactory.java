package com.epam.wca.statistics.util;

import com.epam.wca.statistics.dto.TrainerWorkloadSummary;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class DTOFactory {
    public static TrainerWorkloadSummary convertToDto(
            String username,
            Map<Integer, Map<Integer,Integer>> trainerWorkloads
    ) {
        return new TrainerWorkloadSummary(username, trainerWorkloads);
    }
}
