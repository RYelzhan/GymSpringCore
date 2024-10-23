package com.epam.wca.gym.controller.impl;

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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TraineeControllerImpl implements TraineeControllerDocumentation {
    private final TraineeService traineeService;

    @Value("${gym.api.request.attribute.user}")
    private String authenticatedUserRequestAttributeName;

    @Override
    public TraineeSendDTO getProfile(HttpServletRequest request) {
        var trainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        return DTOFactory.createTraineeSendDTO(trainee);
    }

    @Override
    public TraineeSendDTO updateProfile(
            @RequestBody @Valid TraineeUpdateDTO traineeDTO,
            HttpServletRequest request
    ) {
        var authenticatedTrainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        return traineeService.update(authenticatedTrainee, traineeDTO);
    }

    @Override
    public void deleteProfile(HttpServletRequest request) {
        var authenticatedTrainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        // TODO: invalidation refresh token logic

        traineeService.deleteById(authenticatedTrainee.getId());
    }

    @Override
    public List<TrainerBasicDTO> getNotAssignedTrainers(HttpServletRequest request) {
        var authenticatedTrainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        return traineeService.getListOfNotAssignedTrainers(authenticatedTrainee);
    }

    @Override
    public List<TrainerBasicDTO> updateTrainerList(
            @RequestBody @Valid TraineeTrainersUpdateDTO traineeTrainersUpdateDTO,
            HttpServletRequest request
    ) {
        var authenticatedTrainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        return traineeService.addTrainers(authenticatedTrainee, traineeTrainersUpdateDTO);
    }

    //TODO: transfer all input to RequestParam
    @Override
    public List<TrainingBasicDTO> getTrainings(
            @RequestBody @Valid TraineeTrainingQuery traineeTrainingQuery,
            HttpServletRequest request
    ) {
        var authenticatedTrainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);
        // TODO: READ!!!
        // callable statement
        // principle of least knowledge
        // anonimization of user details
        return traineeService.findTrainingsFiltered(authenticatedTrainee.getId(), traineeTrainingQuery);
    }

    @Override
    public String createTraining(
            @RequestBody @Valid TraineeTrainingCreateDTO trainingDTO,
            HttpServletRequest request
    ) {
        var authenticatedTrainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        traineeService.createTraining(authenticatedTrainee, trainingDTO);

        return "Training Created Successfully";
    }
}
