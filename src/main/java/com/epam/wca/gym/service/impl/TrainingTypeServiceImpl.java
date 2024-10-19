package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.training_type.TrainingTypeBasicDTO;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.exception.InternalErrorException;
import com.epam.wca.gym.repository.TrainingTypeRepository;
import com.epam.wca.gym.service.TrainingTypeService;
import com.epam.wca.gym.util.DTOFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    public TrainingType findByType(String type) {
        return trainingTypeRepository.findTrainingTypeByType(type)
                .orElseThrow(() -> new InternalErrorException("Invalid Training Type choice"));
    }

    @Override
    @Transactional
    public List<TrainingTypeBasicDTO> findAll() {
        return trainingTypeRepository.findAll()
                .stream().map(DTOFactory::createBasicTrainingTypeDTO)
                .toList();
    }
}
