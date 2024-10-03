package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.training_type.TrainingTypeBasicDTO;
import com.epam.wca.gym.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/training")
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingTypeService trainingTypeService;

    @GetMapping("/types")
    public List<TrainingTypeBasicDTO> getTrainingTypes() {
        return trainingTypeService.findAll();
    }
}
