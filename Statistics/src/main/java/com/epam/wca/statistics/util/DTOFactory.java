package com.epam.wca.statistics.util;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerWorkloadSummary;
import lombok.experimental.UtilityClass;

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
}
