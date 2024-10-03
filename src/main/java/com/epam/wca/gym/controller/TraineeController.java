package com.epam.wca.gym.controller;

import com.epam.wca.gym.aop.CheckTrainee;
import com.epam.wca.gym.dto.trainee.TraineeSendDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainersUpdateDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainingCreateDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.training.TraineeTrainingDTO;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.util.DTOFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user/trainee")
@RequiredArgsConstructor
public class TraineeController {
    private final TraineeService traineeService;

    @GetMapping("/profile")
    @CheckTrainee
    @Transactional
    public TraineeSendDTO getTraineeProfile(HttpServletRequest request) {
        var trainee = (Trainee) request.getAttribute("authenticatedUser");

        return DTOFactory.createTraineeSendDTO(trainee);
    }

    // TODO: remove username field from all DTO-s
    // TODO: remove username checks in every controller
    @PutMapping(value = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
    @CheckTrainee
    public TraineeSendDTO updateTraineeProfile(
            @RequestBody @Valid TraineeUpdateDTO traineeDTO,
            HttpServletRequest request
    ) {
        var authenticatedTrainee = (Trainee) request.getAttribute("authenticatedUser");

        return traineeService.update(authenticatedTrainee, traineeDTO);
    }

    @DeleteMapping(value = "/profile")
    @CheckTrainee
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrainee(HttpServletRequest request) {
        var authenticatedTrainee = (Trainee) request.getAttribute("authenticatedUser");

        traineeService.deleteById(authenticatedTrainee.getId());
    }

    @GetMapping("/trainers/available")
    @CheckTrainee
    public List<TrainerBasicDTO> getNotAssignedTrainers(HttpServletRequest request) {
        var authenticatedTrainee = (Trainee) request.getAttribute("authenticatedUser");

        return traineeService.getListOfNotAssignedTrainers(authenticatedTrainee);
    }

    @PutMapping(value = "/trainers/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @CheckTrainee
    public List<TrainerBasicDTO> updateTrainerList(
            @RequestBody @Valid TraineeTrainersUpdateDTO traineeTrainersUpdateDTO,
            HttpServletRequest request
            ) {
        var authenticatedTrainee = (Trainee) request.getAttribute("authenticatedUser");

        return traineeService.addTrainers(authenticatedTrainee, traineeTrainersUpdateDTO);
    }

    @GetMapping("/trainings/filter")
    @CheckTrainee
    public List<TrainingBasicDTO> getTraineeTrainingsList(
            @RequestBody @Valid TraineeTrainingDTO traineeTrainingDTO,
            HttpServletRequest request
            ) {
        var authenticatedTrainee = (Trainee) request.getAttribute("authenticatedUser");

        return traineeService.findTrainingsFiltered(authenticatedTrainee.getId(), traineeTrainingDTO);
    }

    @PostMapping("/trainings")
    @CheckTrainee
    @ResponseStatus(HttpStatus.CREATED)
    public String createTraineeTraining(
            @RequestBody @Valid TraineeTrainingCreateDTO trainingDTO,
            HttpServletRequest request
    ) {
        var authenticatedTrainee = (Trainee) request.getAttribute("authenticatedUser");

        traineeService.createTraining(authenticatedTrainee, trainingDTO);

        return "Training Created Successfully";
    }
}
