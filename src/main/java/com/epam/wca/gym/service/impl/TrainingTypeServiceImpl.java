package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.training_type.TrainingTypeBasicDTO;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.exception.ControllerValidationException;
import com.epam.wca.gym.repository.TrainingTypeRepository;
import com.epam.wca.gym.service.TrainingTypeService;
import com.epam.wca.gym.util.DTOFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    public TrainingType findByType(String type) {
        var trainingType = trainingTypeRepository.findTrainingTypeByType(type);

        if (trainingType == null) {
            throw new ControllerValidationException("Invalid Training Type choice");
        }

        return trainingType;
    }

    @Override
    public List<TrainingTypeBasicDTO> findAll() {
        return trainingTypeRepository.findAll()
                .stream().map(DTOFactory::createBasicTrainingTypeDTO)
                .toList();
    }
}
