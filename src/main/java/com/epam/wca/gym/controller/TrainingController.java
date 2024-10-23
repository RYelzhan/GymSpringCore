package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.training_type.TrainingTypeBasicDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(value = "/trainings", consumes = MediaType.ALL_VALUE)
public interface TrainingController {
    @GetMapping("/types")
    List<TrainingTypeBasicDTO> getTypes();
}
