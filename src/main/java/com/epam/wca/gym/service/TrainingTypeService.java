package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.training_type.TrainingTypeBasicDTO;
import com.epam.wca.gym.entity.TrainingType;

import java.util.List;

public interface TrainingTypeService {
    TrainingType findByType(String type);

    List<TrainingTypeBasicDTO> findAll();
}
