package com.epam.wca.gym.controller;

import com.epam.wca.gym.aop.CheckTrainer;
import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingDTO;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.util.DTOFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "user/trainer")
@RequiredArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;

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
        var authenticatedTrainer = (Trainer) request.getAttribute("authenticatedUser");

        return trainerService.update(authenticatedTrainer, trainerUpdateDTO);
    }

    @DeleteMapping(value = "/profile")
    @CheckTrainer
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrainer(HttpServletRequest request) {
        var authenticatedTrainer = (Trainer) request.getAttribute("authenticatedUser");

        trainerService.deleteById(authenticatedTrainer.getId());
    }

    @GetMapping("/trainings/filter")
    @CheckTrainer
    public List<TrainingBasicDTO> getTrainerTrainingsList(
            @RequestBody @Valid TrainerTrainingDTO trainerTrainingDTO,
            HttpServletRequest request
    ) {
        var authenticatedTrainer = (Trainer) request.getAttribute("authenticatedUser");

        return trainerService.findTrainingsFiltered(authenticatedTrainer.getId(), trainerTrainingDTO);
    }
}
