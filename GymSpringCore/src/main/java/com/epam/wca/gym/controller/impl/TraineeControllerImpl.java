package com.epam.wca.gym.controller.impl;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.gym.controller.documentation.TraineeControllerDocumentation;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TraineeControllerImpl implements TraineeControllerDocumentation {
    private final TraineeService traineeService;

    @Override
    @Logging
    public TraineeSendDTO getProfile(@AuthenticationPrincipal Trainee trainee) {
        return DTOFactory.createTraineeSendDTO(trainee);
    }

    @Override
    @Logging
    public TraineeSendDTO updateProfile(
            @RequestBody @Valid TraineeUpdateDTO traineeDTO,
            @AuthenticationPrincipal Trainee authenticatedTrainee
    ) {
        return traineeService.update(authenticatedTrainee, traineeDTO);
    }

    @Override
    @Logging
    public void deleteProfile(
            @AuthenticationPrincipal Trainee authenticatedTrainee
    ) {
        // TODO: invalidation refresh token logic

        traineeService.deleteById(authenticatedTrainee.getId());
    }

    @Override
    @Logging
    public List<TrainerBasicDTO> getNotAssignedTrainers(
            @AuthenticationPrincipal Trainee authenticatedTrainee
    ) {
        return traineeService.getListOfNotAssignedTrainers(authenticatedTrainee);
    }

    @Override
    public List<TrainerBasicDTO> updateTrainerList(
            @RequestBody @Valid TraineeTrainersUpdateDTO traineeTrainersUpdateDTO,
            @AuthenticationPrincipal Trainee authenticatedTrainee
    ) {
        return traineeService.addTrainers(authenticatedTrainee, traineeTrainersUpdateDTO);
    }

    //TODO: transfer all input to RequestParam
    @Override
    public List<TrainingBasicDTO> getTrainings(
            @RequestBody @Valid TraineeTrainingQuery traineeTrainingQuery,
            @AuthenticationPrincipal Trainee authenticatedTrainee
    ) {
        // TODO: READ!!!
        // callable statement
        // principle of least knowledge
        // anonimization of user details
        return traineeService.findTrainingsFiltered(authenticatedTrainee.getId(), traineeTrainingQuery);
    }

    @Override
    public String createTraining(
            @RequestBody @Valid TraineeTrainingCreateDTO trainingDTO,
            @AuthenticationPrincipal Trainee authenticatedTrainee
    ) {
        traineeService.createTraining(authenticatedTrainee, trainingDTO);

        return "Training Created Successfully";
    }
}
