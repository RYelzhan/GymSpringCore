package com.epam.wca.gym.dto.statistics_dto;

import java.util.Map;

public record TrainerWorkloadSummary(
        String username,
        Map<Integer, Map<Integer, Integer>> trainingSummary
) {
}
