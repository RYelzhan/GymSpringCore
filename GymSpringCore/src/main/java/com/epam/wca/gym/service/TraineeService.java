package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainee.TraineeSendDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainersUpdateDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainingCreateDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.training.TraineeTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.dto.user.UserAuthenticatedDTO;
import com.epam.wca.gym.entity.Trainee;

import java.util.List;

public interface TraineeService {
    TraineeSendDTO getProfile(Trainee trainee);

    UserAuthenticatedDTO save(TraineeRegistrationDTO trainee);

    List<TrainingBasicDTO> findTrainingsFiltered(Long id, TraineeTrainingQuery traineeTrainingQuery);

    TraineeSendDTO update(Trainee trainee, TraineeUpdateDTO traineeUpdateDTO);

    List<TrainerBasicDTO> getListOfNotAssignedTrainers(Trainee trainee);

    void deleteById(Long id);

    void deleteAssociatedTrainings(Long id);

    List<TrainerBasicDTO> addTrainers(Trainee trainee, TraineeTrainersUpdateDTO dto);

    Trainee findById(Long id);

    void createTraining(Trainee trainee, TraineeTrainingCreateDTO trainingDTO);
}
