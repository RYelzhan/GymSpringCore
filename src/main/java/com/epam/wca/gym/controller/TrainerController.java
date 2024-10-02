package com.epam.wca.gym.controller;

import com.epam.wca.gym.aop.CheckTrainer;
import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingDTO;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.exception.ControllerValidationException;
import com.epam.wca.gym.service.impl.TrainerService;
import com.epam.wca.gym.service.impl.TrainingTypeService;
import com.epam.wca.gym.util.DTOFactory;
import com.epam.wca.gym.util.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "user/trainer")
@RequiredArgsConstructor
public class TrainerController {
    @NonNull
    private TrainerService trainerService;
    @NonNull
    private TrainingTypeService trainingTypeService;

    @GetMapping("/profile")
    @CheckTrainer
    public TrainerSendDTO getTrainerProfile(HttpServletRequest request) {
        var authenticatedTrainer = (Trainer) request.getAttribute("authenticatedUser");

        return DTOFactory.createTrainerSendDTO(authenticatedTrainer);
    }

    @PutMapping(value = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
    @CheckTrainer
    public TrainerSendDTO updateTrainerProfile(
            @RequestBody @Valid TrainerUpdateDTO trainerUpdateDTO,
            HttpServletRequest request
    ) {
        var trainingType = trainingTypeService.findByUniqueName(trainerUpdateDTO.trainingType());

        if (trainingType == null) {
            throw new ControllerValidationException("Invalid Training Type choice");
        }

        var authenticatedTrainer = (Trainer) request.getAttribute("authenticatedUser");

        var updatedTrainer = trainerService.update(authenticatedTrainer, trainerUpdateDTO, trainingType);

        return DTOFactory.createTrainerSendDTO(updatedTrainer);
    }

    @DeleteMapping(value = "/profile/{username}")
    @CheckTrainer
    public String deleteTrainer(HttpServletRequest request) {
        var authenticatedTrainer = (Trainer) request.getAttribute("authenticatedUser");

        trainerService.deleteById(authenticatedTrainer.getId());

        return "Trainer Profile Deleted Successfully";
    }

    @GetMapping("/trainings/filter")
    @CheckTrainer
    public List<TrainingBasicDTO> getTraineeTrainingsList(
            @RequestBody @Valid TrainerTrainingDTO trainerTrainingDTO,
            HttpServletRequest request
    ) {
        var authenticatedTrainer = (Trainer) request.getAttribute("authenticatedUser");

        return Filter.filterTrainerTrainings(authenticatedTrainer.getTrainings(),
                        trainerTrainingDTO)
                .stream()
                .map(DTOFactory::createTrainerBasicTrainingDTO)
                .collect(Collectors.toList());
    }
}
