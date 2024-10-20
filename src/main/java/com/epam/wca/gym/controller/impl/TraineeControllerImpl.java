package com.epam.wca.gym.controller.impl;

import com.epam.wca.gym.aop.check.CheckTrainee;
import com.epam.wca.gym.controller.TraineeController;
import com.epam.wca.gym.dto.trainee.TraineeSendDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainersUpdateDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainingCreateDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.training.TraineeTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.util.DTOFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TraineeControllerImpl implements TraineeController {
    private final TraineeService traineeService;

    @Value("${gym.api.request.attribute.user}")
    private String authenticatedUserRequestAttributeName;

    @Override
    @CheckTrainee
    public TraineeSendDTO getTraineeProfile(HttpServletRequest request) {
        var trainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        return DTOFactory.createTraineeSendDTO(trainee);
    }

    @Override
    @CheckTrainee
    public TraineeSendDTO updateTraineeProfile(
            @RequestBody @Valid TraineeUpdateDTO traineeDTO,
            HttpServletRequest request
    ) {
        var authenticatedTrainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        return traineeService.update(authenticatedTrainee, traineeDTO);
    }

    @Override
    @CheckTrainee
    public void deleteTrainee(HttpServletRequest request) {
        var authenticatedTrainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        // TODO: invalidation refresh token logic

        traineeService.deleteById(authenticatedTrainee.getId());
    }

    @Override
    @CheckTrainee
    public List<TrainerBasicDTO> getNotAssignedTrainers(HttpServletRequest request) {
        var authenticatedTrainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        return traineeService.getListOfNotAssignedTrainers(authenticatedTrainee);
    }

    @Override
    @CheckTrainee
    public List<TrainerBasicDTO> updateTrainerList(
            @RequestBody @Valid TraineeTrainersUpdateDTO traineeTrainersUpdateDTO,
            HttpServletRequest request
    ) {
        var authenticatedTrainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        return traineeService.addTrainers(authenticatedTrainee, traineeTrainersUpdateDTO);
    }

    //TODO: transfer all input to RequestParam
    @Override
    @CheckTrainee
    public List<TrainingBasicDTO> getTraineeTrainingsList(
            @RequestBody @Valid TraineeTrainingQuery traineeTrainingQuery,
            HttpServletRequest request
    ) {
        var authenticatedTrainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        return traineeService.findTrainingsFiltered(authenticatedTrainee.getId(), traineeTrainingQuery);
    }

    @Override
    @CheckTrainee
    public String createTraineeTraining(
            @RequestBody @Valid TraineeTrainingCreateDTO trainingDTO,
            HttpServletRequest request
    ) {
        var authenticatedTrainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        traineeService.createTraining(authenticatedTrainee, trainingDTO);

        return "Training Created Successfully";
    }
}
