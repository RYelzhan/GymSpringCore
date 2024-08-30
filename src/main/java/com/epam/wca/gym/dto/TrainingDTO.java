package com.epam.wca.gym.dto;

import com.epam.wca.gym.entity.TrainingType;

import java.util.Date;

public record TrainingDTO(long traineeId, long trainerId, String trainingName, TrainingType trainingType, Date trainingDate, int trainingDuration) {}
