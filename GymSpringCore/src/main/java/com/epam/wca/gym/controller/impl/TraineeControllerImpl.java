package com.epam.wca.gym.controller.impl;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.gym.aop.argument.InsertUser;
import com.epam.wca.gym.aop.argument.InsertUserId;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TraineeControllerImpl implements TraineeControllerDocumentation {
    private final TraineeService traineeService;

    @Override
    @Logging
    public TraineeSendDTO getProfile(@InsertUser Trainee trainee) {
        return traineeService.getProfile(trainee);
    }

    @Override
    @Logging
    public TraineeSendDTO updateProfile(
            @RequestBody @Valid TraineeUpdateDTO traineeDTO,
            @InsertUser Trainee authenticatedTrainee
    ) {
        return traineeService.update(authenticatedTrainee, traineeDTO);
    }

    /**
     * @deprecated refactored Trainee deletion logic to be accessible through message receiver
     * @param id id of trainee getting deleted
     */
    @Deprecated(since = "2.3")
    @Override
    @Logging
    public void deleteProfile(
            @InsertUserId Long id
    ) {
        // TODO: invalidation refresh token logic

        traineeService.deleteById(id);
    }

    @Override
    @Logging
    public List<TrainerBasicDTO> getNotAssignedTrainers(
            @InsertUser Trainee authenticatedTrainee
    ) {
        return traineeService.getListOfNotAssignedTrainers(authenticatedTrainee);
    }

    @Override
    public List<TrainerBasicDTO> updateTrainerList(
            @RequestBody @Valid TraineeTrainersUpdateDTO traineeTrainersUpdateDTO,
            @InsertUser Trainee authenticatedTrainee
    ) {
        return traineeService.addTrainers(authenticatedTrainee, traineeTrainersUpdateDTO);
    }

    //TODO: transfer all input to RequestParam
    @Override
    public List<TrainingBasicDTO> getTrainings(
            @RequestBody @Valid TraineeTrainingQuery traineeTrainingQuery,
            @InsertUser Trainee authenticatedTrainee
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
            @InsertUser Trainee authenticatedTrainee
    ) {
        traineeService.createTraining(authenticatedTrainee, trainingDTO);

        return "Training Creation Started Successfully";
    }
}
