package com.epam.wca.statistics.dto;

import java.util.Map;

public record TrainerWorkloadSummary(
        String username,
        Map<Integer, Map<Integer, Integer>> trainingSummary
) {
}
