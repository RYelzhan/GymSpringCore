package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.training_type.TrainingTypeBasicDTO;
import com.epam.wca.gym.service.deprecated.TrainingTypeServiceOld;
import com.epam.wca.gym.util.DTOFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/training")
@RequiredArgsConstructor
public class TrainingController {
    @NonNull
    private TrainingTypeServiceOld trainingTypeServiceOld;

    @GetMapping("/types")
    public List<TrainingTypeBasicDTO> getTrainingTypes() {
        return trainingTypeServiceOld.findAll()
                .stream()
                .map(DTOFactory::createBasicTrainingTypeDTO)
                .collect(Collectors.toList());
    }
}
