package com.epam.wca.gym.controller;

import com.epam.wca.gym.aop.CheckTrainer;
import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingDTO;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.exception.ForbiddenActionException;
import com.epam.wca.gym.exception.MyValidationException;
import com.epam.wca.gym.service.impl.TrainerService;
import com.epam.wca.gym.service.impl.TrainingTypeService;
import com.epam.wca.gym.util.DTOFactory;
import com.epam.wca.gym.util.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "user/trainer", consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TrainerController {
    @NonNull
    private TrainerService trainerService;
    @NonNull
    private TrainingTypeService trainingTypeService;

    @GetMapping("/profile/{username}")
    @CheckTrainer
    public ResponseEntity<TrainerSendDTO> getTrainerProfile(
            @PathVariable("username") String username,
            HttpServletRequest request
    ) {
        Trainer authenticatedTrainer = (Trainer) request.getAttribute("authenticatedUser");

        if (!authenticatedTrainer.getUserName().equals(username)) {
            throw new ForbiddenActionException("This is not your profile.");
        }

        return new ResponseEntity<>(DTOFactory.createTrainerSendDTO(authenticatedTrainer), HttpStatus.OK);
    }

    @PutMapping("/profile")
    @CheckTrainer
    public ResponseEntity<TrainerSendDTO> updateTrainerProfile(
            @RequestBody @Valid TrainerUpdateDTO trainerUpdateDTO,
            HttpServletRequest request
    ) {
        TrainingType trainingType = trainingTypeService.findByUniqueName(trainerUpdateDTO.trainingType());

        if (trainingType == null) {
            // TODO: add new exception, namely BadRequestException
            throw new MyValidationException("Invalid Training Type choice");
        }

        Trainer authenticatedTrainer = (Trainer) request.getAttribute("authenticatedUser");

        if (!authenticatedTrainer.getUserName().equals(trainerUpdateDTO.username())) {
            throw new ForbiddenActionException("Can not change username");
        }

        Trainer updatedTrainer = trainerService.update(authenticatedTrainer, trainerUpdateDTO, trainingType);

        return new ResponseEntity<>(DTOFactory.createTrainerSendDTO(updatedTrainer), HttpStatus.OK);
    }

    @DeleteMapping("/profile/{username}")
    @CheckTrainer
    public ResponseEntity<String> deleteTrainee(
            @PathVariable("username") String username,
            HttpServletRequest request
    ) {
        Trainer authenticatedTrainer = (Trainer) request.getAttribute("authenticatedUser");

        if (!authenticatedTrainer.getUserName().equals(username)) {
            throw new ForbiddenActionException("This is not your profile.");
        }

        trainerService.deleteById(authenticatedTrainer.getId());

        return ResponseEntity.ok("Trainee Profile Deleted Successfully");
    }

    @GetMapping("/trainings/filter")
    @CheckTrainer
    public ResponseEntity<List<TrainingBasicDTO>> getTraineeTrainingsList(
            @RequestBody @Valid TrainerTrainingDTO trainerTrainingDTO,
            HttpServletRequest request
    ) {
        Trainer authenticatedTrainer = (Trainer) request.getAttribute("authenticatedUser");

        if (!authenticatedTrainer.getUserName().equals(trainerTrainingDTO.username())) {
            throw new ForbiddenActionException("This is not your account.");
        }

        List<TrainingBasicDTO> result = Filter.filterTrainerTrainings(authenticatedTrainer.getTrainings(),
                        trainerTrainingDTO)
                .stream()
                .map(DTOFactory::createTrainerBasicTrainingDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
