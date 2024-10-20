package com.epam.wca.gym.controller.impl;

import com.epam.wca.gym.controller.TrainingController;
import com.epam.wca.gym.dto.training_type.TrainingTypeBasicDTO;
import com.epam.wca.gym.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TrainingControllerImpl implements TrainingController {
    private final TrainingTypeService trainingTypeService;

    @Override
    public List<TrainingTypeBasicDTO> getTypes() {
        return trainingTypeService.findAll();
    }
}
