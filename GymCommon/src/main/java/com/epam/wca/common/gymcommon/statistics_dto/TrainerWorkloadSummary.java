package com.epam.wca.common.gymcommon.statistics_dto;

import java.util.Map;

public record TrainerWorkloadSummary(
        String username,
        Map<Integer, Map<Integer, Integer>> trainingSummary
) {
}
