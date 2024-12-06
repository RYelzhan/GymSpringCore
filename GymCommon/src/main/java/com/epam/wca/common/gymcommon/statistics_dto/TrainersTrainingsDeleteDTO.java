package com.epam.wca.common.gymcommon.statistics_dto;

import com.epam.wca.common.gymcommon.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class TrainersTrainingsDeleteDTO {
    private Map<
                    @NotBlank(message = "Trainer username is required")
                    @Size(min = 2, max = 25, message = "Trainer username must be between 2 and 50 characters")
                        String,
                    Map<
                        ZonedDateTime,
                        @NotNull(message = "Training duration is required")
                        @Min(value = 10, message = "Minimum duration is 10 minutes")
                            Integer
                    >
            > trainingsInfo = new HashMap<>();

    @JsonIgnore
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT);

    @JsonAnySetter
    public void setTrainingsInfo(String trainerUsername, Map<String, Integer> value) {
        Map<ZonedDateTime, Integer> parsedMap = new HashMap<>();

        value.forEach((dateString, duration) -> parsedMap.put(ZonedDateTime.parse(dateString, FORMATTER), duration));

        trainingsInfo.put(trainerUsername, parsedMap);
    }

    @JsonAnyGetter
    public Map<String, Map<String, Integer>> getTrainingsInfoJSON() {
        Map<String, Map<String, Integer>> serializedMap = new HashMap<>();

        trainingsInfo.forEach((trainerUsername, trainingInfo) -> {
            Map<String, Integer> nestedMap = new HashMap<>();
            trainingInfo.forEach((zonedDateTime, duration) ->
                    nestedMap.put(zonedDateTime.format(FORMATTER), duration)
            );
            serializedMap.put(trainerUsername, nestedMap);
        });

        return serializedMap;
    }

    @JsonIgnore
    public Map<String, Map<ZonedDateTime, Integer>> getTrainingsInfo() {
        return trainingsInfo;
    }
}
